package cn.aberic.avast.shape.model;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 图片形状基类
 * 作者：Aberic on 16/2/19 22:36
 * 邮箱：abericyang@gmail.com
 */
public abstract class Shape implements IShape {

    public enum ShapeType {
        /** 普通形状 */
        NORMAL,
        /** 圆形 */
        CIRCULAR,
        /** 圆角 */
        CORNER
    }

    @Override
    public void makeShape(ImageView imageView, Bitmap bitmap) {
        makeShape(imageView, bitmap, AVast.obtain().image.getConfig().displayConfig.shapeConfig);
    }

    @Override
    public void makeShape(ImageView imageView, int resId) {
        makeShape(imageView, resId, AVast.obtain().image.getConfig().displayConfig.shapeConfig);
    }

    @Override
    public void makeShape(ImageView imageView, Bitmap bitmap, ShapeConfig config) {
        imageView.setImageDrawable(draw(config, bitmap));
    }

    @Override
    public void makeShape(ImageView imageView, int resId, ShapeConfig config) {
        imageView.setImageDrawable(draw(config, AVast.obtain().util.bitmap.decodeResource(resId)));
    }

    private RoundedBitmapDrawable draw(ShapeConfig config, Bitmap bitmap) {
        return paint(config, getBitmapDrawable(config, bitmap));
    }

    private RoundedBitmapDrawable getBitmapDrawable(ShapeConfig config, Bitmap bitmap) {
        return RoundedBitmapDrawableFactory.create(config.res, bitmap);// 创建RoundedBitmapDrawable对象
    }

    abstract RoundedBitmapDrawable paint(ShapeConfig config, RoundedBitmapDrawable bitmapDrawable);

}
