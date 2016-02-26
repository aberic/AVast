package cn.aberic.avast.core;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import cn.aberic.avast.imageLoader.cache.BitmapCache;
import cn.aberic.avast.imageLoader.cache.DoubleCache;
import cn.aberic.avast.imageLoader.config.ImageLoaderConfig;
import cn.aberic.avast.util.AUtilConfig;

/**
 * 作者：Aberic on 16/2/18 20:19
 * 邮箱：abericyang@gmail.com
 */
public class AVastConfig implements ConfigVast {

    public AUtilConfig utilConfig;
    ImageLoaderConfig imageLoaderConfig;

    public AVastConfig setCache(BitmapCache cache) {
        imageLoaderConfig.setCache(cache);
        return this;
    }

    public AVastConfig setLoading(int resId) {
        imageLoaderConfig.displayConfig.loadingResId = resId;
        return this;
    }

    public AVastConfig setFail(int resId) {
        imageLoaderConfig.displayConfig.failedResId = resId;
        return this;
    }

    public AVastConfig setImageViewScaleType(ImageView.ScaleType scaleType) {
        imageLoaderConfig.displayConfig.scaleType = scaleType;
        return this;
    }

    public AVastConfig setRes(Resources res) {
        imageLoaderConfig.displayConfig.shapeConfig.res = res;
        return this;
    }

    @Override
    public AVastConfig init(Context context) {
        utilConfig = new AUtilConfig().init(context);
        imageLoaderConfig = new ImageLoaderConfig().init(context)
                .setImageViewScaleType(ImageView.ScaleType.CENTER_CROP )
                .setCache(new DoubleCache(context))// 默认双缓存机制
                .setRes(context.getResources());
        return this;
    }
}
