package cn.aberic.avast.view.lockView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.aberic.avast.R;
import cn.aberic.avast.core.AVast;

/**
 * 作者：Aberic on 16/2/23 11:31
 * 邮箱：abericyang@gmail.com
 * 感谢：http://blog.csdn.net/lmj623565791/article/details/36236113
 */
public class GestureLockViewGroup extends RelativeLayout {

    /** 保存所有的GestureLockView */
    private GestureLockView[] mGestureLockViews;
    /** 每个边上的GestureLockView的个数 */
    private int mCount = 4;
    /** 存储答案 */
    private String mAnswer = "";
    /** 保存用户选中的GestureLockView的id */
    private ArrayList<Integer> mChoose = new ArrayList<>();
    /** 画布 */
    private Paint mPaint;
    /** GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 ) */
    private int mGestureLockViewWidth;
    /** 手指没有按住时内圆颜色 */
    private int mColorNoFingerInner = 0xFF939090;
    /** 手指没有按住时外圆颜色 */
    private int mColorNoFingerOuter = 0xFFE0DBDB;
    /** 手指按住时内外圆颜色 */
    private int mColorFingerOn = 0xFF378FC9;
    /** 手指离开时内外圆颜色(密码正确) */
    private int mColorFingerUpRight = 0xFF2E8B57;
    /** 手指离开时内外圆颜色(密码错误) */
    private int mColorFingerUpWrong = 0xFFFF0000;
    private Path mPath;
    /** 指引线的开始位置x */
    private int mLastPathX;
    /** 指引线的开始位置y */
    private int mLastPathY;
    /** 指引下的结束位置 */
    private Point mTmpTarget = new Point();
    /** 最大尝试次数 */
    private int mMaxTryTimes = 4;
    /** 回调接口 */
    private OnGestureLockViewListener mOnGestureLockViewListener;

    public enum LockInputResult {
        /** 手势输入正确 */
        LOCK_INPUT_RIGHT,
        /** 手势输入错误 */
        LOCK_INPUT_WRONG,
        /** 手势输入少于4位 */
        LOCK_INPUT_LESS,
        /** 手势输入次数超过5次 */
        LOCK_INPUT_OVER_TIME,
        /** 当按压屏幕时 */
        LOCK_INPUT_PRESS
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义参数值
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup, 0, 0);
        int count = attributes.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = attributes.getIndex(i);
            if (attr == R.styleable.GestureLockViewGroup_color_no_finger_inner_circle) {
                mColorNoFingerInner = attributes.getColor(attr, mColorNoFingerInner);
            } else if (attr == R.styleable.GestureLockViewGroup_color_no_finger_outer_circle) {
                mColorNoFingerOuter = attributes.getColor(attr, mColorNoFingerOuter);
            } else if (attr == R.styleable.GestureLockViewGroup_color_finger_on) {
                mColorFingerOn = attributes.getColor(attr, mColorFingerOn);
            } else if (attr == R.styleable.GestureLockViewGroup_color_finger_up_right) {
                mColorFingerUpRight = attributes.getColor(attr, mColorFingerUpRight);
            } else if (attr == R.styleable.GestureLockViewGroup_color_finger_up_wrong) {
                mColorFingerUpRight = attributes.getColor(attr, mColorFingerUpWrong);
            } else if (attr == R.styleable.GestureLockViewGroup_count) {
                mCount = attributes.getInt(attr, mCount);
            } else if (attr == R.styleable.GestureLockViewGroup_tryTimes) {
                mMaxTryTimes = attributes.getInt(attr, mMaxTryTimes);
            }
        }
        attributes.recycle();
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        // mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setColor(Color.parseColor("#aaffffff"));
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        // 初始化mGestureLockViews
        if (null == mGestureLockViews) {
            mGestureLockViews = new GestureLockView[mCount * mCount];
            // 计算每个GestureLockView的宽度
            mGestureLockViewWidth = (int) (3 * mWidth * 1.0f / (6 * mCount + 1));
            // 计算每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
            int mMarginBetweenLockView = mGestureLockViewWidth * 3 / 5;
            // 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);
            for (int i = 0; i < mGestureLockViews.length; i++) {
                // 初始化每个GestureLockView
                mGestureLockViews[i] = new GestureLockView(getContext(), mColorNoFingerInner, mColorNoFingerOuter
                        , mColorFingerOn, mColorFingerUpRight, mColorFingerUpWrong);
                mGestureLockViews[i].setId(i + 1);
                // 设置参数，主要是定位GestureLockView间的位置
                LayoutParams lockerParams = new LayoutParams(mGestureLockViewWidth, mGestureLockViewWidth);
                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF, mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW, mGestureLockViews[i - mCount].getId());
                }
                int leftMargin = 0;
                int topMargin = 0;
                // 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                if (i >= 0 && i < mCount) {// 第一行
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0) {// 第一列
                    leftMargin = mMarginBetweenLockView;
                }
                // 设置左上右下的边距
                lockerParams.setMargins(leftMargin, topMargin, mMarginBetweenLockView, mMarginBetweenLockView);
                mGestureLockViews[i].setMode(GestureLockView.Mode.MODE_NO_FINGER);
                addView(mGestureLockViews[i], lockerParams);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();// 当前触碰 X 坐标
        int y = (int) event.getY();// 当前触碰 Y 坐标
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                reset();
                mOnGestureLockViewListener.onLockInputResult(LockInputResult.LOCK_INPUT_PRESS);
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.mMaxTryTimes == 0){
                    return false;
                }
                mPaint.setColor(mColorFingerOn);
                mPaint.setAlpha(50);
                GestureLockView child = getChildIdByPos(x, y);
                if (null != child) {
                    int cid = child.getId();
                    if (!mChoose.contains(cid)) {
                        mChoose.add(cid);
                        child.setMode(GestureLockView.Mode.MODE_FINGER_ON);
                        if (null != mOnGestureLockViewListener) {
                            // 设置指引线的起点
                            mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                            mLastPathY = child.getTop() / 2 + child.getBottom() / 2;
                            if (mChoose.size() == 1) {// 当前为第一个添加或选中
                                mPath.moveTo(mLastPathX, mLastPathY);
                            } else {// 不止一个,则将两者用线连上
                                mPath.lineTo(mLastPathX, mLastPathY);
                            }
                        }
                    }
                }
                mTmpTarget.x = x;
                mTmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP:
                boolean isSuccess = checkAnswer();
                mPaint.setColor(isSuccess ? mColorFingerUpRight : mColorFingerUpWrong);
                mPaint.setAlpha(50);
                // 将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;
                // 改变修改所有已连接或触碰子元素的状态为UP
                changeItemMode(isSuccess ? GestureLockView.Mode.MODE_FINGER_UP_RIGHT : GestureLockView.Mode.MODE_FINGER_UP_WRONG);
                // 计算每个元素中箭头需要旋转的角度
                int count = mChoose.size() - 1;
                for (int i = 0; i < count; i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);

                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);

                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();
                    // 计算角度
                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree(angle);
                }
                if (mChoose.size() >= 4 && null != mOnGestureLockViewListener) {// 当选择不小于4且回调不为空时才能记录密码
                    this.mMaxTryTimes--;
                    mOnGestureLockViewListener.onLockInputResult(isSuccess
                            ? LockInputResult.LOCK_INPUT_RIGHT
                            : this.mMaxTryTimes == 0 ? LockInputResult.LOCK_INPUT_OVER_TIME : LockInputResult.LOCK_INPUT_WRONG);
                } else if (mChoose.size() > 0 && mChoose.size() < 4 && null != mOnGestureLockViewListener) {
                    mOnGestureLockViewListener.onLockInputResult(LockInputResult.LOCK_INPUT_LESS);
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 修改所有已连接或触碰 GestureLockView 状态
     *
     * @param mode
     *         状态
     */
    private void changeItemMode(GestureLockView.Mode mode) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (mChoose.contains(gestureLockView.getId())) {
                gestureLockView.setMode(mode);
            }
        }
    }

    /** 重置必要信息 */
    public void reset() {
        mChoose.clear();
        mPath.reset();
        for (GestureLockView gestureLockView : mGestureLockViews) {
            gestureLockView.setMode(GestureLockView.Mode.MODE_NO_FINGER);
            gestureLockView.setArrowDegree(-1);
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     *
     * @return 正确与否
     */
    private boolean checkAnswer() {
        return mAnswer.equals(AVast.obtain().util.md5.getMD5(mChoose.toString()));
    }

    /**
     * 检查当前左边是否在child中
     *
     * @param child
     *         当前触碰 GestureLockView
     * @param x
     *         当前触碰 X 坐标
     * @param y
     *         当前触碰 Y 坐标
     *
     * @return 存在与否
     */
    private boolean checkPositionInChild(View child, int x, int y) {
        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (mGestureLockViewWidth * 0.15);
        return x >= child.getLeft() + padding
                && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding;
    }

    /**
     * 通过x,y获得落入的GestureLockView
     *
     * @param x
     *         当前触碰 X 坐标
     * @param y
     *         当前触碰 Y 坐标
     *
     * @return 当前触碰 GestureLockView
     */
    private GestureLockView getChildIdByPos(int x, int y) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }
        return null;
    }

    /**
     * 设置回调接口
     *
     * @param listener
     *         回调
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }

    /** 获取手势输入字符顺序 */
    public ArrayList<Integer> getChoose() {
        return mChoose;
    }

    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     *         答案的方法
     */
    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }

    /** 剩余尝试次数 */
    public int getMaxTryTimes() {
        return mMaxTryTimes;
    }

    /**
     * 设置最大尝试次数(剩余尝试次数)
     *
     * @param maxTryTimes
     *         次数
     */
    public void setMaxTryTimes(int maxTryTimes) {
        this.mMaxTryTimes = maxTryTimes;
    }

    /**
     * 设置最多实验次数
     *
     * @param boundary
     *         实验次数
     */
    public void setUnMatchExceedBoundary(int boundary) {
        this.mMaxTryTimes = boundary;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0) {
            if (mLastPathX != 0 && mLastPathY != 0)
                canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x, mTmpTarget.y, mPaint);
        }
    }

    public interface OnGestureLockViewListener {

        /**
         * 手势输入结果监听
         *
         * @param lockResultType
         *         输入结果类型
         */
        void onLockInputResult(LockInputResult lockResultType);
    }
}
