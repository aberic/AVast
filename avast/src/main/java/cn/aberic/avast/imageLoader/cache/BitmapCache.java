package cn.aberic.avast.imageLoader.cache;

import android.graphics.Bitmap;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 图片缓存对象
 * 作者：Aberic on 16/2/16 21:11
 * 邮箱：abericyang@gmail.com
 */
public interface BitmapCache {

    Bitmap get(BitmapRequest key);

    void put(BitmapRequest key, Bitmap bitmap);

    void clearCache();

}
