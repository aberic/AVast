package cn.aberic.avast.shape.factory;

import cn.aberic.avast.shape.model.CircularShape;
import cn.aberic.avast.shape.model.CornerShape;
import cn.aberic.avast.shape.model.IShape;
import cn.aberic.avast.shape.model.NormalShape;
import cn.aberic.avast.shape.model.Shape;

/**
 * 创建图片形状（工厂类）
 * 作者：Aberic on 16/2/20 00:01
 * 邮箱：abericyang@gmail.com
 */
public class CreateShape implements ICreateShape {

    @Override
    public IShape createShape(Shape.ShapeType type) {
        switch (type) {
            case CIRCULAR:
                return new CircularShape();
            case CORNER:
                return new CornerShape();
            default:
                return new NormalShape();
        }
    }
}
