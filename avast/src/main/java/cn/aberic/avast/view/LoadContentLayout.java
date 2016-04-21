package cn.aberic.avast.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.aberic.avast.R;

/**
 * 作者：Aberic on 16/4/19 21:05
 * 邮箱：abericyang@gmail.com
 */
public class LoadContentLayout extends FrameLayout {

    public enum ContentState {
        VIEW_CONTENT,
        VIEW_ERROR,
        VIEW_EMPTY,
        VIEW_LOADING
    }

    private LayoutInflater mInflater;
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private ContentState mViewState = ContentState.VIEW_CONTENT;

    private LoadingListener mLoadingListener;

    public LoadContentLayout(Context context) {
        super(context, null);
    }

    public LoadContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.LoadContentLayout);

        int loadingResId = attributes.getResourceId(R.styleable.LoadContentLayout_loadingView, -1);
        if (loadingResId > -1) {
            mLoadingView = mInflater.inflate(loadingResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }

        int emptyViewResId = attributes.getResourceId(R.styleable.LoadContentLayout_emptyView, -1);
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }

        int errorViewResId = attributes.getResourceId(R.styleable.LoadContentLayout_errorView, -1);
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        }

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewState == ContentState.VIEW_ERROR) {
                    mViewState = ContentState.VIEW_LOADING;
                    setView();
                    if (null != mLoadingListener) {
                        mLoadingListener.load();
                    }
                }
            }
        });

        // 默认初始状态加载中
        mViewState = ContentState.VIEW_LOADING;
        setViewState(mViewState);

        attributes.recycle();
    }

    private void setView() {
        switch (mViewState) {
            case VIEW_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("LoadContentLayout : Loading View");
                }

                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("LoadContentLayout : Empty View");
                }

                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;

            case VIEW_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("LoadContentLayout : Error View");
                }

                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_CONTENT:
            default:
                if (mContentView == null) {
                    throw new NullPointerException("LoadContentLayout : Content View");
                }

                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    public View getView(ContentState state) {
        switch (state) {
            case VIEW_CONTENT:
                return mContentView;
            case VIEW_EMPTY:
                return mEmptyView;
            case VIEW_ERROR:
                return mErrorView;
            case VIEW_LOADING:
                return mLoadingView;
        }
        return null;
    }

    /**
     * 根据状态设置选中视图
     *
     * @param viewState
     *         状态
     */
    public void setViewState(ContentState viewState) {
        if (viewState != mViewState) {
            mViewState = viewState;
            setView();
        }
    }

    public ContentState getViewState() {
        return mViewState;
    }

    /**
     * 检查传入的 ContentView 是否有效
     *
     * @param view
     *         view
     *
     * @return 是否有效
     */
    private boolean isValidContentView(View view) {
        return !(null != mContentView && mContentView != view) && view != mLoadingView && view != mErrorView && view != mEmptyView;
    }

    /**
     * 设置当前视图被赋予的状态
     *
     * @param view
     *         当前视图
     * @param state
     *         状态
     * @param switchToState
     *         是否根据状态切换至指定视图
     */
    public void setViewForState(View view, ContentState state, boolean switchToState) {
        switch (state) {
            case VIEW_CONTENT:
                if (null != mContentView) {
                    removeView(mContentView);
                }
                mContentView = view;
                addView(mContentView);
                break;
            case VIEW_EMPTY:
                if (null != mEmptyView) {
                    removeView(mEmptyView);
                }
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case VIEW_ERROR:
                if (null != mErrorView) {
                    removeView(mErrorView);
                }
                mErrorView = view;
                addView(mErrorView);
                break;
            case VIEW_LOADING:
                if (null != mLoadingView) {
                    removeView(mLoadingView);
                }
                mLoadingView = view;
                addView(mLoadingView);
                break;
        }
        if (switchToState) {
            setViewState(state);
        }
    }

    /**
     * 设置当前视图被赋予的状态
     *
     * @param view
     *         当前视图
     * @param state
     *         状态
     */
    public void setViewForState(View view, ContentState state) {
        setViewForState(view, state, false);
    }

    /**
     * 设置当前视图被赋予的状态
     *
     * @param layoutRes
     *         Layout resource id
     * @param state
     *         状态
     * @param switchToState
     *         是否根据状态切换至指定视图
     */
    public void setViewForState(@LayoutRes int layoutRes, ContentState state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    public void setViewForState(@LayoutRes int layoutRes, ContentState state) {
        setViewForState(layoutRes, state, false);
    }

    /**
     * 重新加载错误界面
     *
     * @param loadingListener
     *         加载监听
     */
    public void setReLoad(LoadingListener loadingListener) {
        mLoadingListener = loadingListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) {
            throw new IllegalArgumentException("LoadContentLayout : Content view is not defined");
        }
        setView();
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    public interface LoadingListener {
        void load();
    }

}
