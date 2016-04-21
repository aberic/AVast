package cn.aberic.avast.cache.impl;

import android.graphics.Bitmap;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.BitmapCacheRequest;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.base.DiskCache;

/**
 * 作者：Aberic on 16/2/17 19:54
 * 邮箱：abericyang@gmail.com
 */
public class BitmapDoubleCache implements BaseCache<Bitmap> {

    // private static final String TAG = "DoubleCache";

    BitmapDiskCache diskCache = new BitmapDiskCache();
    BitmapMemoryCache memoryCache = new BitmapMemoryCache();

    private void saveBitmapIntoMemory(BitmapCacheRequest request, Bitmap bitmap) {
        if (null != bitmap) {
            memoryCache.put(request, bitmap);
        }
    }

    @Override
    public Bitmap get(CacheRequest key) {
        Bitmap bitmap = memoryCache.get(key);
        if (null != bitmap) {
            // Log.d(TAG, "获取到内存中图片缓存|" + ((BitmapCacheRequest) key).imageUri);
        } else if (DiskCache.obtain().isDiskLruCacheCreated()) {
            bitmap = diskCache.get(key);
            if (null != bitmap) {
                // Log.d(TAG, "获取到磁盘中图片缓存|" + ((BitmapCacheRequest) key).imageUri);
                saveBitmapIntoMemory(((BitmapCacheRequest) key), bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void put(CacheRequest key, Bitmap bitmap) {
        // 如果磁盘缓存能够创建,则先缓存入磁盘,待下次读取磁盘缓存数据时,将会对 bitmap 进行优化处理,过后方可将优化后的 bitmap 放入内存缓存中
        if (DiskCache.obtain().isDiskLruCacheCreated()) {
            diskCache.put(key, bitmap);
        } else {// 如果磁盘缓存不能创建,则直接缓存入内存中
            memoryCache.put(key, bitmap);
        }
    }

}
