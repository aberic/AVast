package cn.aberic.avast.shape.model;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 默认形状
 * 作者：Aberic on 16/2/19 22:46
 * 邮箱：abericyang@gmail.com
 */
public class NormalShape extends Shape {

    @Override
    RoundedBitmapDrawable paint(ShapeConfig config, RoundedBitmapDrawable bitmapDrawable) {
        return bitmapDrawable;
    }
}
