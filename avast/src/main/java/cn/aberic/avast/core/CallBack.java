package cn.aberic.avast.core;

import android.view.View;

/**
 * 作者：Aberic on 16/2/19 15:41
 * 邮箱：abericyang@gmail.com
 */
public interface CallBack {

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
