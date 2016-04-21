package cn.aberic.avast.cache.core;

import android.content.Context;
import android.graphics.Bitmap;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.impl.BitmapDoubleCache;
import cn.aberic.avast.cache.impl.StringDoubleCache;
import cn.aberic.avast.cache.base.DiskCache;
import cn.aberic.avast.cache.base.MemoryCache;

/**
 * 作者：Aberic on 16/3/1 09:22
 * 邮箱：abericyang@gmail.com
 */
public class ACache {

    /** 字符串缓存 */
    public volatile BaseCache<String> stringCache;
    /** 图片缓存 */
    public volatile BaseCache<Bitmap> bitmapCache;

    public ACache(Context context) {
        DiskCache.obtain().init(context);// 初始化磁盘缓存
        MemoryCache.obtain().initMemoryCache();// 初始化内存缓存
        stringCache = new StringDoubleCache();// 默认字符串双缓存机制
        bitmapCache = new BitmapDoubleCache();// 默认图片双缓存机制
    }

    public long getCacheSize() {
        return DiskCache.obtain().mDiskLruCache.size();
    }

    public void clearMemoryCache() {
        MemoryCache.obtain().clearCache();
    }

    public void clearDiskCache() {
        DiskCache.obtain().clearCache();
    }

}
