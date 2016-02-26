package cn.aberic.avast.imageLoader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 作者：Aberic on 16/2/17 19:54
 * 邮箱：abericyang@gmail.com
 */
public class DoubleCache implements BitmapCache {

    private static final String TAG = "DoubleCache";

    DiskCache mDiskCache;
    MemoryCache mMemoryCache = new MemoryCache();

    public DoubleCache(Context context) {
        mDiskCache = DiskCache.getInstance(context);
    }

    private void saveBitmapIntoMemory(BitmapRequest request, Bitmap bitmap) {
        if (null != bitmap) {
            mMemoryCache.put(request, bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        Bitmap bitmap = mMemoryCache.get(key);
        if (null != bitmap) {
//            Log.d(TAG, "获取到内存中图片缓存|" + key.imageUri);
        } else if (mDiskCache.isDiskLruCacheCreated()) {
            bitmap = mDiskCache.get(key);
            if (null != bitmap) {
//                Log.d(TAG, "获取到磁盘中图片缓存|" + key.imageUri);
                saveBitmapIntoMemory(key, bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        // 如果磁盘缓存能够创建,则先缓存入磁盘,待下次读取磁盘缓存数据时,将会对 bitmap 进行优化处理,过后方可将优化后的 bitmap 放入内存缓存中
        if (mDiskCache.isDiskLruCacheCreated()) {
            mDiskCache.put(key, bitmap);
        } else {// 如果磁盘缓存不能创建,则直接缓存入内存中
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public void clearCache() {
        mDiskCache.clearCache();
        mMemoryCache.clearCache();
    }
}
