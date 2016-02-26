package cn.aberic.avast.imageLoader.loader;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 作者：Aberic on 16/2/17 00:41
 * 邮箱：abericyang@gmail.com
 */
public class FileLoader extends AbsLoader {

    @Override
    protected void onLoadImage(BitmapRequest request) {
        String imagePath = Uri.parse(request.imageUri).getPath();
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = AVast.obtain().util.imageCompressSize.decodeSampledBitmapFromFile(
                    imagePath, request.getImageViewWidth(), request.getImageViewHeight());
            cacheBitmap(request, bitmap);// 缓存图片
            deliveryToUIThread(request, bitmap);
        }
    }
}
