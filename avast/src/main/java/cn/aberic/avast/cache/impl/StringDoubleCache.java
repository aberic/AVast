package cn.aberic.avast.cache.impl;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.request.StringCacheRequest;
import cn.aberic.avast.cache.base.DiskCache;

/**
 * 作者：Aberic on 16/2/17 19:54
 * 邮箱：abericyang@gmail.com
 */
public class StringDoubleCache implements BaseCache<String> {

    // private static final String TAG = "StringDoubleCache";

    StringDiskCache stringDiskCache = new StringDiskCache();
    StringMemoryCache stringMemoryCache = new StringMemoryCache();

    private void saveBitmapIntoMemory(StringCacheRequest request, String result) {
        if (null != result) {
            stringMemoryCache.put(request, result);
        }
    }

    @Override
    public String get(CacheRequest key) {
        String result = stringMemoryCache.get(key);
        if (null != result) {
            // Log.d(TAG, "获取到内存中String|" + ((StringCacheRequest) key).uri + "|result = " + result);
        } else if (DiskCache.obtain().isDiskLruCacheCreated()) {
            result = stringDiskCache.get(key);
            if (null != result) {
                // Log.d(TAG, "获取到磁盘中String|" + ((StringCacheRequest) key).uri + "|result = " + result);
                saveBitmapIntoMemory(((StringCacheRequest) key), result);
            }
        }
        return result;
    }

    @Override
    public void put(CacheRequest key, String result) {
        // 如果磁盘缓存能够创建,则先缓存入磁盘,待下次读取磁盘缓存数据时,将会对 bitmap 进行优化处理,过后方可将优化后的 bitmap 放入内存缓存中
        if (DiskCache.obtain().isDiskLruCacheCreated()) {
            stringDiskCache.put(key, result);
        } else {// 如果磁盘缓存不能创建,则直接缓存入内存中
            stringMemoryCache.put(key, result);
        }
    }
}
