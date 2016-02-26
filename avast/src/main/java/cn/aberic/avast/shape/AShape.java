package cn.aberic.avast.shape;

import cn.aberic.avast.shape.factory.CreateShape;
import cn.aberic.avast.shape.factory.ICreateShape;
import cn.aberic.avast.shape.model.IShape;
import cn.aberic.avast.shape.model.Shape;

/**
 * 作者：Aberic on 16/2/19 22:56
 * 邮箱：abericyang@gmail.com
 */
public class AShape {

    public IShape circularShape;
    public IShape cornerShape;

    public AShape() {
        ICreateShape createShape = new CreateShape();
        circularShape = createShape.createShape(Shape.ShapeType.CIRCULAR);
        cornerShape = createShape.createShape(Shape.ShapeType.CORNER);
    }

}
