package cn.aberic.avast.cache.impl;

import android.graphics.Bitmap;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;

/**
 * 没有缓存
 * 作者：Aberic on 16/2/16 23:12
 * 邮箱：abericyang@gmail.com
 */
public class NoCache implements BaseCache<Bitmap> {

    @Override
    public Bitmap get(CacheRequest key) {
        return null;
    }

    @Override
    public void put(CacheRequest key, Bitmap bitmap) {

    }

}
