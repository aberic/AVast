package cn.aberic.avast.view.lockView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * 小圆圈/箭头 子 View
 * 作者：Aberic on 16/2/23 10:49
 * 邮箱：abericyang@gmail.com
 * 感谢：http://blog.csdn.net/lmj623565791/article/details/36236113
 */
public class GestureLockView extends View {

    /** 圆点三种状态(默认/按住/抬起) */
    enum Mode {
        MODE_NO_FINGER, MODE_FINGER_ON, MODE_FINGER_UP_RIGHT, MODE_FINGER_UP_WRONG
    }

    /** 圆点默认状态 */
    private Mode mMode = Mode.MODE_NO_FINGER;
    /** 外圆半径 */
    private int mRadius;
    /** 内圆的半径 = mInnerRadius * mRadius */
    private float mInnerRadius = 0.3F;
    /** 画笔宽度 */
    private int mStrokeWidth;
    /** 圆心坐标 X */
    private int mCenterX;
    /** 圆心坐标 Y */
    private int mCenterY;
    /** 画布 */
    private Paint mPaint;
    /** 箭头方向 */
    private int mArrowDegree = -1;
    /** 箭头… */
    private Path mArrowPath;
    /** 手指没有按住时内圆颜色 */
    private int mColorNoFingerInner;
    /** 手指没有按住时外圆颜色 */
    private int mColorNoFingerOuter;
    /** 手指按住时内外圆颜色 */
    private int mColorFingerOn;
    /** 手指离开时内外圆颜色(密码正确) */
    private int mColorFingerUpRight;
    /** 手指离开时内外圆颜色(密码错误) */
    private int mColorFingerUpWrong;

    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOuter
            , int colorFingerOn, int mColorFingerUpRight, int colorFingerUpWrong) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOuter = colorNoFingerOuter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUpRight = mColorFingerUpRight;
        this.mColorFingerUpWrong = colorFingerUpWrong;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 使画布位图抗锯齿的标志
        mArrowPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = mWidth < mHeight ? mWidth : mHeight;// 取长和宽中的较小值
        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;
        // 开始绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        float arrowRate = 0.333f;// 箭头（小三角最长边的一半长度 = arrowRate * mWidth / 2 ）
        float mArrowLength = mWidth / 2 * arrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mMode) {
            case MODE_NO_FINGER:
                // 绘制外圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorNoFingerOuter);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setColor(mColorNoFingerInner);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerRadius, mPaint);
                break;
            case MODE_FINGER_ON:
                // 绘制外圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mColorFingerOn);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerRadius, mPaint);
                break;
            case MODE_FINGER_UP_RIGHT:
                // 绘制外圆
                mPaint.setColor(mColorFingerUpRight);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerRadius, mPaint);
                drawArrow(canvas);
                break;
            case MODE_FINGER_UP_WRONG:
                // 绘制外圆
                mPaint.setColor(mColorFingerUpWrong);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerRadius, mPaint);
                drawArrow(canvas);
                break;
        }
    }

    /**
     * 绘制箭头
     *
     * @param canvas
     *         画布
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(mArrowDegree, mCenterX, mCenterY);
            canvas.drawPath(mArrowPath, mPaint);
            canvas.restore();
        }
    }

    /**
     * 设置当前模式并重新绘图
     *
     * @param mode
     *         模式
     */
    public void setMode(Mode mode) {
        this.mMode = mode;
        invalidate();
    }

    /**
     * 设置箭头角度
     *
     * @param degree
     *         角度
     */
    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }
}
