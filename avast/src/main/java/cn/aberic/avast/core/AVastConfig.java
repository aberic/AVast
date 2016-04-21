package cn.aberic.avast.core;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import cn.aberic.avast.imageLoader.config.ImageLoaderConfig;

/**
 * 作者：Aberic on 16/2/18 20:19
 * 邮箱：abericyang@gmail.com
 */
public class AVastConfig implements ConfigVast {

    protected Context mContext;
    protected ImageLoaderConfig imageLoaderConfig;

    /**
     * 设置 ImageLoader 加载中图片
     *
     * @param resId
     *         资源 id
     *
     * @return 本类
     */
    public AVastConfig setLoading(int resId) {
        imageLoaderConfig.setLoading(resId);
        return this;
    }

    /**
     * 设置 ImageLoader 加载失败图片
     *
     * @param resId
     *         资源 id
     *
     * @return 本类
     */
    public AVastConfig setFail(int resId) {
        imageLoaderConfig.setFail(resId);
        return this;
    }

    /**
     * 设置 ImageLoader 加载图片 ScaleType
     *
     * @param scaleType
     *         scaleType
     *
     * @return 本类
     */
    public AVastConfig setImageViewScaleType(ImageView.ScaleType scaleType) {
        imageLoaderConfig.setImageViewScaleType(scaleType);
        return this;
    }

    public AVastConfig setRes(Resources res) {
        imageLoaderConfig.displayConfig.shapeConfig.res = res;
        return this;
    }

    @Override
    public AVastConfig init(Context context) {
        mContext = context;
        imageLoaderConfig = new ImageLoaderConfig().init(mContext)
                .setImageViewScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRes(mContext.getResources());
        return this;
    }
}
