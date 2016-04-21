package cn.aberic.avast.cache.impl;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.request.StringCacheRequest;
import cn.aberic.avast.cache.base.MemoryCache;

/**
 * 图片内存缓存
 * <p/>
 * 作者：Aberic on 16/2/4 11:08
 * 邮箱：abericyang@gmail.com
 */
public class StringMemoryCache implements BaseCache<String> {

    @Override
    public String get(CacheRequest key) {
        return MemoryCache.obtain().memoryStringCache.get(((StringCacheRequest) key).uriMd5);
    }

    @Override
    public void put(CacheRequest key, String string) {
        MemoryCache.obtain().memoryStringCache.put(((StringCacheRequest) key).uriMd5, string);
    }

}
