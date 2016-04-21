package cn.aberic.avast.cache.base;

import cn.aberic.avast.cache.request.CacheRequest;

/**
 * 图片缓存对象
 * 作者：Aberic on 16/2/16 21:11
 * 邮箱：abericyang@gmail.com
 */
public interface BaseCache<T> {

    T get(CacheRequest key);

    void put(CacheRequest key, T t);

}
