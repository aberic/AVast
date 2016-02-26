package cn.aberic.avast.shape.model;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 圆形图片
 * 作者：Aberic on 16/2/19 22:44
 * 邮箱：abericyang@gmail.com
 */
public class CircularShape extends Shape {

    @Override
    RoundedBitmapDrawable paint(ShapeConfig config, RoundedBitmapDrawable bitmapDrawable) {
        bitmapDrawable.setCircular(true);// 设置圆形
        return bitmapDrawable;
    }
}
