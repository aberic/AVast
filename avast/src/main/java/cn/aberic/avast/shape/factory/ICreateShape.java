package cn.aberic.avast.shape.factory;

import cn.aberic.avast.shape.model.IShape;
import cn.aberic.avast.shape.model.Shape;

/**
 * 创建形状接口（工厂接口）
 * 作者：Aberic on 16/2/20 00:00
 * 邮箱：abericyang@gmail.com
 */
public interface ICreateShape {

    IShape createShape(Shape.ShapeType type);

}
