package cn.aberic.avast.cache.request;

import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.imageLoader.config.DisplayConfig;
import cn.aberic.avast.imageLoader.core.AImage;
import cn.aberic.avast.imageLoader.utils.ImageViewHelper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 网络请求类.
 * 作者：Aberic on 16/2/16 21:22
 * 邮箱：abericyang@gmail.com
 */
public class BitmapCacheRequest implements CacheRequest {

    /** ImageView 引用 */
    Reference<ImageView> mImageViewRef;
    /** 图片加载前后默认配置 */
    public DisplayConfig displayConfig;
    /** 图片加载监听器 */
    public AImage.ImageListener imageListener;
    /** 图片加载路径 */
    public String imageUri = "";
    /** 图片缓存 key(url 可能会导致部分问题,因此转为md5形式作为 key 使用) */
    public String imageUriMD5 = "";
    public Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;

    public BitmapCacheRequest(ImageView imageView, String uri, DisplayConfig config, AImage.ImageListener listener) {
        mImageViewRef = new WeakReference<>(imageView);// 弱引用
        displayConfig = config;
        imageListener = listener;
        imageUri = uri;
        // 设置 ImageView 的 tag 为图片的 uri
        imageView.setTag(imageUri);
        imageUriMD5 = AVast.obtain().util.md5.getMD5(imageUri);
        checkUriFormat(imageUri);
    }

    private void checkUriFormat(String uri) {
        if (uri.substring(uri.lastIndexOf("/") + 1).toUpperCase().contains("PNG")) {
            format = Bitmap.CompressFormat.PNG;
        } else if (uri.substring(uri.lastIndexOf("/") + 1).toUpperCase().contains("WEBP")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                format = Bitmap.CompressFormat.WEBP;
            }
        }
    }

    /** 判断当前请求的 ImageView 的 tag 是否与 imageUri 相同 */
    public boolean isImageViewTagValid() {
        return null != mImageViewRef.get() && mImageViewRef.get().getTag().equals(imageUri);
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(mImageViewRef.get());
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(mImageViewRef.get());
    }

}
