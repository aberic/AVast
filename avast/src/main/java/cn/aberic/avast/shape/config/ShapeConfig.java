package cn.aberic.avast.shape.config;

import android.content.Context;
import android.content.res.Resources;

import cn.aberic.avast.core.ConfigVast;
import cn.aberic.avast.shape.model.Shape;

/**
 * 图片图形配置器
 * 作者：Aberic on 16/2/20 00:24
 * 邮箱：abericyang@gmail.com
 */
public class ShapeConfig implements IShapeConfig, ConfigVast {

    /** 形状类型 */
    public Shape.ShapeType shapeType = Shape.ShapeType.NORMAL;
    /** Resources 一般设置为 getResource() */
    public Resources res = null;
    /** 圆角半径 */
    public float cornerRadius = 0f;
    /** 开启反锯齿 */
    public boolean aa = false;

    @Override
    public ShapeConfig init(Context context) {
        this.res = context.getResources();
        return this;
    }

    public ShapeConfig(Resources res, float cornerRadius, boolean aa) {
        this.res = res;
        this.cornerRadius = cornerRadius;
        this.aa = aa;
    }

    @Override
    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void setAa(boolean aa) {
        this.aa = aa;
    }
}
