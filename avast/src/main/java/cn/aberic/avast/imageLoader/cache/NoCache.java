package cn.aberic.avast.imageLoader.cache;

import android.graphics.Bitmap;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 没有缓存
 * 作者：Aberic on 16/2/16 23:12
 * 邮箱：abericyang@gmail.com
 */
public class NoCache implements BitmapCache {

    @Override
    public Bitmap get(BitmapRequest key) {
        return null;
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {

    }

    @Override
    public void clearCache() {

    }
}
