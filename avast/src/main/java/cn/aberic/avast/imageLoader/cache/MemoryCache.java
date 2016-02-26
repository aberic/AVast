package cn.aberic.avast.imageLoader.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 图片内存缓存
 * <p/>
 * 作者：Aberic on 16/2/4 11:08
 * 邮箱：abericyang@gmail.com
 */
public class MemoryCache implements BitmapCache {

    private static final String TAG = "MemoryCache";
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCache() {
        // 初始化 LRU 缓存
        initMemoryCache();
    }

    private void initMemoryCache() {
        /* 计算可使用的最大内存 */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);// 除以1024转换为KB
        /* 取1/8可用内存作为缓存 */
        final int cacheSize = maxMemory / 4;
        Log.d(TAG, "maxMemory = " + maxMemory + "|cacheSize = " + cacheSize);
        /*
         * 初始化内存缓存并重写 sizeOf 方法.
         * sizeOf 方法用于计算缓存对象的大小,这里大小的单位需要和总容量 cacheSize 的单位一致.
         * 而 sizeOf 方法完成对 Bitmap 对象大小的计算后,除以1024以转换为 KB.
         */
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        return mMemoryCache.get(key.imageUri);
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        mMemoryCache.put(key.imageUri, bitmap);
    }

    @Override
    public void clearCache() {
        if (null != mMemoryCache) {
            if (mMemoryCache.size() > 0) {
                mMemoryCache.evictAll();
            }
        }
    }
}
