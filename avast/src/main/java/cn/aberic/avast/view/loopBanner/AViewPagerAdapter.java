package cn.aberic.avast.view.loopBanner;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.aberic.avast.R;
import cn.aberic.avast.core.AVast;
import cn.aberic.avast.core.CallBack;
import cn.aberic.avast.view.loopBanner.entity.BannerItem;

public class AViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> mViews;
    private ArrayList<BannerItem> mBannerItems;
    private CallBack.OnItemClickListener mListener;

    public AViewPagerAdapter(ArrayList<View> views, ArrayList<BannerItem> bannerItems) {
        this.mViews = views;
        this.mBannerItems = bannerItems;
    }

    public void setOnPagerClick(CallBack.OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mViews.get(position));// 删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {// 这个方法用来实例化页卡
        // TODO Auto-generated method stub
        View view = mViews.get(position);// 获取当前view
        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.item_image_view_img);
        AVast.obtain().image.bindImage(imageView, mBannerItems.get(position).imgPath);
        view.setOnClickListener(new OnClickListener() {// 当前view单击事件

            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, position);
            }
        });
        container.removeView(mViews.get(position));
        container.addView(mViews.get(position));// 添加页卡
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mViews.size();// 返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == object;// 官方提示这样写
    }

}
