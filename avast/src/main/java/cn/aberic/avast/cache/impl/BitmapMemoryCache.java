package cn.aberic.avast.cache.impl;

import android.graphics.Bitmap;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.base.MemoryCache;
import cn.aberic.avast.cache.request.BitmapCacheRequest;

/**
 * 图片内存缓存
 * <p/>
 * 作者：Aberic on 16/2/4 11:08
 * 邮箱：abericyang@gmail.com
 */
public class BitmapMemoryCache implements BaseCache<Bitmap> {

    @Override
    public Bitmap get(CacheRequest key) {
        return MemoryCache.obtain().memoryBitmapCache.get(((BitmapCacheRequest) key).imageUri);
    }

    @Override
    public void put(CacheRequest key, Bitmap bitmap) {
        MemoryCache.obtain().memoryBitmapCache.put(((BitmapCacheRequest) key).imageUri, bitmap);
    }

}
