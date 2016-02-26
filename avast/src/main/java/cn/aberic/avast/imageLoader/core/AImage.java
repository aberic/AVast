package cn.aberic.avast.imageLoader.core;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import cn.aberic.avast.imageLoader.cache.BitmapCache;
import cn.aberic.avast.imageLoader.cache.MemoryCache;
import cn.aberic.avast.imageLoader.cache.NoCache;
import cn.aberic.avast.imageLoader.config.DisplayConfig;
import cn.aberic.avast.imageLoader.config.ImageLoaderConfig;
import cn.aberic.avast.imageLoader.loader.LoaderManager;
import cn.aberic.avast.imageLoader.request.BitmapRequest;
import cn.aberic.avast.shape.model.Shape;

/**
 * 图片加载类入口,支持url和本地uri形式的加载,根据图片路径来判断是网络图片还是本地图片
 * 作者：Aberic on 16/2/16 21:31
 * 邮箱：abericyang@gmail.com
 */
public class AImage {

    /** 缓存 */
    private volatile BitmapCache mCache = new MemoryCache();
    /** 图片加载配置对象 */
    private ImageLoaderConfig mConfig;

    public AImage(ImageLoaderConfig config) {
        mConfig = config;
        mCache = mConfig.bitmapCache;
        checkCache();
    }

    private void checkCache() {
        if (null == mCache) {
            mCache = new NoCache();
        }
    }

    public void bindImage(ImageView imageView, String uri) {
        bindImage(imageView, uri, null, null);
    }

    public void bindImage(ImageView imageView, String uri, DisplayConfig config) {
        bindImage(imageView, uri, config, null);
    }

    public void bindImage(ImageView imageView, String uri, ImageListener listener) {
        bindImage(imageView, uri, null, listener);
    }

    public void bindImage(ImageView imageView, String uri, DisplayConfig config, ImageListener listener) {
        BitmapRequest request = new BitmapRequest(imageView, uri, config, listener);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = request.displayConfig != null ? request.displayConfig : mConfig.displayConfig;
        request.displayConfig.shapeConfig.shapeType = Shape.ShapeType.NORMAL;
        // 加载图片
        LoaderManager.getInstance().getLoader(parseSchema(uri)).loadImage(request);
    }

    public void bindImageInCorner(ImageView imageView, String uri) {
        BitmapRequest request = new BitmapRequest(imageView, uri, null, null);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = mConfig.displayConfig;
        request.displayConfig.shapeConfig.shapeType = Shape.ShapeType.CORNER;
        // 加载图片
        LoaderManager.getInstance().getLoader(parseSchema(uri)).loadImage(request);
    }

    public void bindImageInCircular(ImageView imageView, String uri) {
        BitmapRequest request = new BitmapRequest(imageView, uri, null, null);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = mConfig.displayConfig;
        request.displayConfig.shapeConfig.shapeType = Shape.ShapeType.CIRCULAR;
        // 加载图片
        LoaderManager.getInstance().getLoader(parseSchema(uri)).loadImage(request);
    }

    public ImageLoaderConfig getConfig() {
        return mConfig;
    }

    public void clearCache() {
        mCache.clearCache();
    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            Log.e("AImage", "### wrong scheme, image uri is : " + uri);
        }
        return "";
    }

    /** 图片加载监听器 **/
    public interface ImageListener {
        void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

}
