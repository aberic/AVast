package cn.aberic.avast.cache.base;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 图片内存缓存
 * <p/>
 * 作者：Aberic on 16/2/4 11:08
 * 邮箱：abericyang@gmail.com
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    /** 图片内存缓存 */
    public LruCache<String, Bitmap> memoryBitmapCache;
    /** 字符串内存缓存 */
    public LruCache<String, String> memoryStringCache;

    private static MemoryCache diskCache;

    public static MemoryCache obtain() {
        if (null == diskCache) {
            synchronized (DiskCache.class) {
                if (null == diskCache) {
                    diskCache = new MemoryCache();
                }
            }
        }
        return diskCache;
    }

    public void initMemoryCache() {
        /* 计算可使用的最大内存 */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);// 除以1024转换为KB
        /* 取1/4可用图片内存作为缓存 */
        final int imageCacheSize = maxMemory / 4;
        /* 取1/8可用字符串内存作为缓存 */
        final int stringCacheSize = maxMemory / 8;
        Log.d(TAG, "maxMemory = " + maxMemory + "|imageCacheSize = " + imageCacheSize + "|stringCacheSize = " + stringCacheSize);
        /*
         * 初始化内存缓存并重写 sizeOf 方法.
         * sizeOf 方法用于计算缓存对象的大小,这里大小的单位需要和总容量 cacheSize 的单位一致.
         * 而 sizeOf 方法完成对 Bitmap 对象大小的计算后,除以1024以转换为 KB.
         */
        memoryBitmapCache = new LruCache<String, Bitmap>(imageCacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        memoryStringCache = new LruCache<>(stringCacheSize);
    }

    public void clearCache() {
        if (null != MemoryCache.obtain().memoryStringCache) {
            if (MemoryCache.obtain().memoryStringCache.size() > 0) {
                MemoryCache.obtain().memoryStringCache.evictAll();
            }
        }
    }
}
