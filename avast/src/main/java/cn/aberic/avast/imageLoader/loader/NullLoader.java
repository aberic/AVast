package cn.aberic.avast.imageLoader.loader;

import android.util.Log;

import cn.aberic.avast.cache.request.BitmapCacheRequest;

/**
 * 作者：Aberic on 16/2/17 00:00
 * 邮箱：abericyang@gmail.com
 */
public class NullLoader extends AbsLoader {

    @Override
    protected void onLoadImage(BitmapCacheRequest request) {
        Log.e(NullLoader.class.getSimpleName(), "### wrong schema, your image uri is : "
                + request.imageUri);
    }
}
