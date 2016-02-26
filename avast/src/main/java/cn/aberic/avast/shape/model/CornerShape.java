package cn.aberic.avast.shape.model;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import cn.aberic.avast.shape.config.ShapeConfig;

/**
 * 圆角图片
 * 作者：Aberic on 16/2/19 22:46
 * 邮箱：abericyang@gmail.com
 */
public class CornerShape extends Shape {

    @Override
    RoundedBitmapDrawable paint(ShapeConfig config, RoundedBitmapDrawable bitmapDrawable) {
        bitmapDrawable.setCornerRadius(config.cornerRadius);
        bitmapDrawable.setAntiAlias(config.aa);
        return bitmapDrawable;
    }
}
