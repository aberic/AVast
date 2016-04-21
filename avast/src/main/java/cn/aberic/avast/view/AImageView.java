package cn.aberic.avast.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.aberic.avast.core.AVast;

/**
 * 可加载网络图片的 ImageView 容器
 * 作者：Aberic on 16/3/1 17:57
 * 邮箱：abericyang@gmail.com
 */
public class AImageView extends ImageView {

    public AImageView(Context context) {
        super(context);
    }

    public AImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置加载图片
     * @param url 允许类型(http://-mipmap://-drawable://-file://)
     */
    public void setImageUrl(String url) {
        AVast.obtain().image.bindImage(this, url);
    }
}
