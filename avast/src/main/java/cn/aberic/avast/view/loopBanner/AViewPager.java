package cn.aberic.avast.view.loopBanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.aberic.avast.R;
import cn.aberic.avast.core.CallBack;
import cn.aberic.avast.util.TimerUtil;

import java.util.ArrayList;

/**
 * 自定义 ViewPager 实现轮播等效果
 * 作者：Aberic on 16/1/27 17:23
 * 邮箱：abericyang@gmail.com
 */
public class AViewPager extends RelativeLayout {

    private ViewPager mViewPager;
    private AViewPagerAdapter viewPagerAdapter;
    /** dot view 容器 */
    private LinearLayout mLinearLayout;
    /** 当前 ViewPager 所在 index,初始化默认为1 */
    private int nowIndex = 1;
    /** banner list */
    private ArrayList<String> mImgPaths;
    private ArrayList<View> views;
    private ArrayList<View> dots;
    /** 定时封装器 */
    private TimerUtil timerUtil;

    /** 显示圆点指标与否 */
    private boolean showDots = false;
    /** 循环与否 */
    private boolean isLoop = false;
    /** 自动播放与否 */
    private boolean isAutoPlay = false;
    /** 距离底部高度 */
    private int marginBottom = 0;
    /** 小圆点背景色 */
    private int dotBackground = R.color.PNG_FIVE;
    /** 圆点未选中颜色 */
    private int dotRes = R.drawable.image_indicator_dot;
    /** 圆点选中颜色 */
    private int dotChooseRes = R.drawable.image_indicator_dot_choose;
    /** 圆点间距 */
    private int dotMargins = 0;

    public AViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义参数值
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AViewPager, 0, 0);
        int count = attributes.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = attributes.getIndex(i);
            if (attr == R.styleable.AViewPager_showDots) {
                showDots = attributes.getBoolean(attr, showDots);
            } else if (attr == R.styleable.AViewPager_isLoop) {
                isLoop = attributes.getBoolean(attr, isLoop);
            } else if (attr == R.styleable.AViewPager_isAutoPlay) {
                isAutoPlay = attributes.getBoolean(attr, isAutoPlay);
            } else if (attr == R.styleable.AViewPager_marginBottom) {
                marginBottom = attributes.getInteger(attr, marginBottom);
            } else if (attr == R.styleable.AViewPager_dotBackground) {
                dotBackground = attributes.getColor(attr, dotBackground);
            } else if (attr == R.styleable.AViewPager_dotRes) {
                dotRes = attributes.getResourceId(attr, dotBackground);
            } else if (attr == R.styleable.AViewPager_dotChooseRes) {
                dotChooseRes = attributes.getResourceId(attr, dotBackground);
            } else if (attr == R.styleable.AViewPager_dotMargins) {
                dotMargins = attributes.getInteger(attr, dotMargins);
            }
        }
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_ad_view_pager, this, true);
        mViewPager = (ViewPager) findViewById(R.id.custom_ad_view_pager_for_id);
        if (showDots) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub_custom_ad_view_pager_for_dot);
            viewStub.inflate();
            mLinearLayout = (LinearLayout) findViewById(R.id.custom_ad_view_pager_for_dot);
            LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, marginBottom);
            lp.addRule(ALIGN_PARENT_BOTTOM);
            mLinearLayout.setLayoutParams(lp);
            mLinearLayout.setPadding(0, 8, 0, 8);
            mLinearLayout.setBackgroundColor(dotBackground);
            dots = new ArrayList<>();
        }

        mImgPaths = new ArrayList<>();
        views = new ArrayList<>();
        viewPagerAdapter = new AViewPagerAdapter(views, mImgPaths);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * 监听 ViewPager item 切换
     *
     * @param onPageChangeListener
     *         切换回调
     */
    public void initAViewPagerOnItemChange(final ViewPager.OnPageChangeListener onPageChangeListener) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                onPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        });
    }

    /**
     * 给 ViewPager 赋值图片并监听 ViewPager item 单击
     *
     * @param imgPaths
     *         图片 list
     * @param listener
     *         监听回调
     */
    public void initAViewPagerOnItemClick(ArrayList<String> imgPaths, CallBack.OnItemClickListener listener) {
        mImgPaths.clear();
        views.clear();
        if (null == imgPaths || 0 == imgPaths.size()) {
            return;
        }
        if (imgPaths.size() == 1) {
            isLoop = false;
            isAutoPlay = false;
        }
        mImgPaths.addAll(imgPaths);
        int size = mImgPaths.size();
        if (isLoop) {
            mImgPaths.add(size, mImgPaths.get(0));
            mImgPaths.add(0, mImgPaths.get(size - 1));
            size = mImgPaths.size();
        }
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_image_view, null);
            views.add(view);
        }
        viewPagerAdapter.notifyDataSetChanged();
        if (showDots) {
            setDot(size);
        }
        initListener(size);
        viewPagerAdapter.setOnPagerClick(listener);
        if (isLoop) {
            mViewPager.setCurrentItem(1);
        }
    }

    private void moveToNext(int size, int position) {
        if (position == 0) {
            mViewPager.setCurrentItem(size - 2, false);
            nowIndex = size - 2;
        } else if (position == size - 1) {
            mViewPager.setCurrentItem(1, false);
            nowIndex = 1;
        } else {
            nowIndex = position;
        }
        if (showDots) {
            setRightDots(nowIndex - 1);
        }
    }

    private void setRightDots(int nowPosition) {
        dots.get(nowPosition).setBackgroundResource(dotChooseRes);
        int length = dots.size();
        for (int i = 0; i < length; i++) {
            if (i != nowPosition)
                dots.get(i).setBackgroundResource(dotRes);
        }
    }

    private void initListener(final int size) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isLoop) {
                    moveToNext(size, position);
                } else {
                    setRightDots(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setDot(int size) {
        if (isLoop) {
            size -= 2;
        }
        for (int i = 0; i < size; i++) {
            View dotItem = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(dotMargins, 0, dotMargins, 0);
            dotItem.setBackgroundResource(dotRes);
            dotItem.setLayoutParams(params);
            mLinearLayout.addView(dotItem);
            dots.add(dotItem);
        }
    }

    public void startAutoLoop() {
        if (isAutoPlay) {
            if (null == timerUtil) {
                timerUtil = new TimerUtil(false);
            }
            timerUtil.start(3 * 1000, 3 * 1000);
            timerUtil.monitorTimer(new TimerUtil.TimerListener() {
                @Override
                public void onTimerListener() {
                    nowIndex++;
                    mViewPager.setCurrentItem(nowIndex);
                }
            });
        }
    }

    public void stopAutoLoop() {
        if (isAutoPlay) {
            if (!timerUtil.isStop()) {
                timerUtil.stop();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAutoLoop();
        super.onDetachedFromWindow();
    }
}
