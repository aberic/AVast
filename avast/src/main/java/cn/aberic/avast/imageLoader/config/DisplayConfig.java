package cn.aberic.avast.imageLoader.config;

import android.content.Context;
import android.widget.ImageView;

import cn.aberic.avast.core.ConfigVast;
import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 加载图片时的loading资源以及加载失败的图片
 * 作者：Aberic on 16/2/16 21:24
 * 邮箱：abericyang@gmail.com
 */
public class DisplayConfig implements ConfigVast {

    /** 全局上下文 */
    public Context context;
    /** 正在加载中所显示图片资源 id */
    public int loadingResId = -1;
    /** 加载失败或加载无效时显示图片资源 id */
    public int failedResId = -1;
    /** 图片显示模式(比例/位置) */
    public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    public ShapeConfig shapeConfig;

    @Override
    public DisplayConfig init(Context context) {
        this.context = context;
        shapeConfig = new ShapeConfig(context.getResources(), 20, true);
        return this;
    }
}
