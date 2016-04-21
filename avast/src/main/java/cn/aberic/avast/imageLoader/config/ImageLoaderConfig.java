package cn.aberic.avast.imageLoader.config;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import cn.aberic.avast.core.ConfigVast;

/**
 * 使用ImageLoader前都会通过一个配置类来设置一些基本的东西，比如加载中的图片、加载失败的图片、缓存策略等等.
 * <p/>
 * ImageLoader配置类
 * 作者：Aberic on 16/2/16 21:08
 * 邮箱：abericyang@gmail.com
 */
public class ImageLoaderConfig implements ConfigVast {

    /** 加载图片时的loading和加载失败的图片配置对象 */
    public DisplayConfig displayConfig;

    @Override
    public ImageLoaderConfig init(Context context) {
        displayConfig = new DisplayConfig().init(context);
        return this;
    }

    public ImageLoaderConfig setLoading(int resId) {
        displayConfig.loadingResId = resId;
        return this;
    }

    public ImageLoaderConfig setFail(int resId) {
        displayConfig.failedResId = resId;
        return this;
    }

    public ImageLoaderConfig setImageViewScaleType(ImageView.ScaleType scaleType) {
        displayConfig.scaleType = scaleType;
        return this;
    }

    public ImageLoaderConfig setRes(Resources res) {
        displayConfig.shapeConfig.res = res;
        return this;
    }
}
