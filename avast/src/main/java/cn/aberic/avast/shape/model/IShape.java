package cn.aberic.avast.shape.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 图片形状接口
 * 作者：Aberic on 16/2/19 22:34
 * 邮箱：abericyang@gmail.com
 */
public interface IShape {

    void makeShape(ImageView imageView, int resId, ShapeConfig config);

    void makeShape(ImageView imageView, Bitmap bitmap, ShapeConfig config);

    void makeShape(ImageView imageView, Bitmap bitmap);

    void makeShape(ImageView imageView, int resId);

}
