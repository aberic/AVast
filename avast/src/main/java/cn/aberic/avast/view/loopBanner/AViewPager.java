package cn.aberic.avast.view.loopBanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.aberic.avast.R;
import cn.aberic.avast.core.CallBack;
import cn.aberic.avast.view.loopBanner.entity.BannerItem;

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
    private ArrayList<BannerItem> mBannerItems;
    private ArrayList<View> views;
    private ArrayList<View> dots;

    private Timer mTimer;
    private TimerTask mTimerTask;

    /** 显示圆点指标与否 */
    private boolean showDots = false;
    /** 循环与否 */
    private boolean isLoop = false;
    /** 自动播放与否 */
    private boolean isAutoPlay = false;

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
            dots = new ArrayList<>();
        }

        mBannerItems = new ArrayList<>();
        views = new ArrayList<>();
        viewPagerAdapter = new AViewPagerAdapter(views, mBannerItems);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(1);
    }

    public void initAViewPager(ArrayList<BannerItem> bannerItems, CallBack.OnItemClickListener listener) {
        mBannerItems.clear();
        views.clear();
        mBannerItems.addAll(bannerItems);
        int size = mBannerItems.size();
        if (isLoop) {
            mBannerItems.add(size, mBannerItems.get(0));
            mBannerItems.add(0, mBannerItems.get(size - 1));
            size = mBannerItems.size();
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
            dots.get(nowIndex - 1).setBackgroundResource(R.drawable.image_indicator_dot_choose);
            int length = dots.size();
            for (int i = 0; i < length; i++) {
                if (i != nowIndex - 1)
                    dots.get(i).setBackgroundResource(R.drawable.image_indicator_dot);
            }
        }
    }

    private void initListener(final int size) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isLoop) {
                    moveToNext(size, position);
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
            dotItem.setBackgroundResource(R.drawable.image_indicator_dot);
            mLinearLayout.addView(dotItem);
            dots.add(dotItem);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    nowIndex++;
                    mViewPager.setCurrentItem(nowIndex);
                    break;

                default:
                    break;
            }
        }
    };

    public void startAutoLoop() {
        if (null == mTimer && isAutoPlay) {
            mTimer = new Timer();
        }
        if (null == mTimerTask && isAutoPlay) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendMessage(handler.obtainMessage(1));
                }
            };
        }
        if (isAutoPlay) {
            mTimer.schedule(mTimerTask, 3 * 1000, 3 * 1000);
        }
    }

    public void stopAutoLoop() {
        if (isAutoPlay) {
            if (null != mTimerTask) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            if (null != mTimer) {
                mTimer.cancel();
                mTimer = null;
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
