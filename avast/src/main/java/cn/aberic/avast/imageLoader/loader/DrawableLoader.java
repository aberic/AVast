package cn.aberic.avast.imageLoader.loader;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 作者：Aberic on 16/2/17 00:41
 * 邮箱：abericyang@gmail.com
 */
public class DrawableLoader extends AbsLoader {

    @Override
    protected void onLoadImage(BitmapRequest request) {
        int resId = request.displayConfig.context.getResources().getIdentifier(request.imageUri.split("://")[1], "drawable", request.displayConfig.context.getPackageName());
        request.getImageView().setImageResource(resId);
    }
}
