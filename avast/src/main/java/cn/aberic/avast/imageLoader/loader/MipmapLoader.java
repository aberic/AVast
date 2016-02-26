package cn.aberic.avast.imageLoader.loader;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 作者：Aberic on 16/2/17 00:41
 * 邮箱：abericyang@gmail.com
 */
public class MipmapLoader extends AbsLoader {

    @Override
    protected void onLoadImage(BitmapRequest request) {
        int resId = request.displayConfig.context.getResources().getIdentifier(request.imageUri.split("://")[1], "mipmap", request.displayConfig.context.getPackageName());
        updateImageView(request, AVast.obtain().util.bitmap.decodeResource(resId));
    }
}
