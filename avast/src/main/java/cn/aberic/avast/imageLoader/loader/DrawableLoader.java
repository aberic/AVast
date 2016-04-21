package cn.aberic.avast.imageLoader.loader;

import android.os.Process;

import cn.aberic.avast.cache.request.BitmapCacheRequest;
import cn.aberic.avast.core.AVast;

/**
 * 作者：Aberic on 16/2/17 00:41
 * 邮箱：abericyang@gmail.com
 */
public class DrawableLoader extends AbsLoader {

    @Override
    protected void onLoadImage(final BitmapCacheRequest request) {
        AVast.obtain().threadPool.submitFixed(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                int resId = request.displayConfig.context.getResources().getIdentifier(request.imageUri.split("://")[1], "drawable", request.displayConfig.context.getPackageName());
                cacheBitmap(request, AVast.obtain().util.bitmap.decodeResource(resId));// 缓存图片
                deliveryToUIThread(request, AVast.obtain().util.bitmap.decodeResource(resId));
            }
        });
    }
}
