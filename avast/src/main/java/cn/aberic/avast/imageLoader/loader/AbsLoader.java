package cn.aberic.avast.imageLoader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.imageLoader.config.DisplayConfig;
import cn.aberic.avast.cache.request.BitmapCacheRequest;

/**
 * 图片加载过程:
 * 1.判断缓存中是否含有该图片;
 * 2.如果有则将图片直接投递到UI线程，并且更新UI；
 * 3.如果没有缓存，则从对应的地方获取到图片，并且将图片缓存起来，然后再将结果投递给UI线程，更新UI；
 * <p/>
 * AbsLoader 抽象类
 * 作者：Aberic on 16/2/16 23:25
 * 邮箱：abericyang@gmail.com
 */
public abstract class AbsLoader implements Loader {

    /** 图片缓存 */
    private static final BaseCache<Bitmap> mCache = AVast.obtain().cache.bitmapCache;

    @Override
    public void loadImage(BitmapCacheRequest request) {
        // 从缓存中获取
        Bitmap bitmap = mCache.get(request);
        if (null == bitmap) {// 没有缓存
            showLoading(request);
            onLoadImage(request);// 加载图片
        } else {
            updateImageView(request, bitmap);
        }
    }

    /**
     * 加载图片
     *
     * @param request
     *         图片加载请求
     */
    protected abstract void onLoadImage(BitmapCacheRequest request);

    /**
     * 缓存图片
     *
     * @param request
     *         图片请求
     * @param bitmap
     *         图片
     */
    protected void cacheBitmap(BitmapCacheRequest request, Bitmap bitmap) {
        if (null != bitmap && null != mCache) {
            synchronized (mCache) {
                mCache.put(request, bitmap);
            }
        }
    }

    /**
     * 显示加载中的视图
     *
     * @param request
     *         图片加载请求
     */
    protected void showLoading(final BitmapCacheRequest request) {
        final ImageView imageView = request.getImageView();
        if (request.isImageViewTagValid() && hasLoadingPlaceHolder(request.displayConfig)) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(request.displayConfig.loadingResId);
                }
            });
        }
    }

    /**
     * 将结果投递到UI,更新ImageView
     *
     * @param request
     *         图片请求
     * @param bitmap
     *         图片
     */
    protected void deliveryToUIThread(final BitmapCacheRequest request, final Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        if (null != imageView) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    updateImageView(request, bitmap);
                }
            });
        }
    }

    /**
     * 更新ImageView
     *
     * @param request
     *         图片请求
     * @param bitmap
     *         图片
     */
    protected void updateImageView(BitmapCacheRequest request, Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        imageView.setScaleType(null != request.displayConfig.scaleType ? request.displayConfig.scaleType : ImageView.ScaleType.CENTER_CROP);
        String uri = request.imageUri;
        if (null != bitmap && imageView.getTag().equals(uri)) {// 加载成功
            showImageShape(true, request, imageView, bitmap);
        }
        if (null == bitmap && hasFailedPlaceHolder(request.displayConfig)) {// 加载失败
            showImageShape(false, request, imageView, null);
        }
        if (null != request.imageListener) {// 回调
            request.imageListener.onComplete(imageView, bitmap, uri);
        }
    }

    private void showImageShape(boolean isBitmap, BitmapCacheRequest request, ImageView imageView, Bitmap bitmap) {
        switch (request.displayConfig.shapeConfig.shapeType) {
            case NORMAL:
                if (isBitmap) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(request.displayConfig.failedResId);
                }
                break;
            case CIRCULAR:
                if (isBitmap) {
                    AVast.obtain().shape.circularShape.makeShape(imageView, bitmap);
                } else {
                    AVast.obtain().shape.circularShape.makeShape(imageView, request.displayConfig.failedResId);
                }
                break;
            case CORNER:
                if (isBitmap) {
                    AVast.obtain().shape.cornerShape.makeShape(imageView, bitmap);
                } else {
                    AVast.obtain().shape.cornerShape.makeShape(imageView, request.displayConfig.failedResId);
                }
                break;
        }
    }

    private boolean hasLoadingPlaceHolder(DisplayConfig displayConfig) {
        return null != displayConfig && displayConfig.loadingResId > 0;
    }

    private boolean hasFailedPlaceHolder(DisplayConfig displayConfig) {
        return null != displayConfig && displayConfig.failedResId > 0;
    }
}
