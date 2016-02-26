package cn.aberic.avast.core;

import cn.aberic.avast.http.core.AHttp;
import cn.aberic.avast.imageLoader.config.ImageLoaderConfig;
import cn.aberic.avast.imageLoader.core.AImage;
import cn.aberic.avast.pool.AThreadPool;
import cn.aberic.avast.shape.AShape;
import cn.aberic.avast.util.AUtilConfig;

/**
 * AVast 框架总入口
 * 作者：Aberic on 16/2/18 09:44
 * 邮箱：abericyang@gmail.com
 */
public class AVast {

    private static AVast aVast;

    /** Http 操作核心 */
    public AHttp http;
    /** ImageLoader 操作核心 */
    public AImage image;
    /** 线程池操作核心 */
    public AThreadPool threadPool;
    /** 一般公共方法操作核心 */
    public AUtilConfig util;
    /**Bitmap 形状操作核心*/
    public AShape shape;

    public AVast init(AVastConfig config) {
        threadPool = new AThreadPool();// 必须实例化
        checkConfig(config.utilConfig, config.imageLoaderConfig);
        util = config.utilConfig;
        shape = new AShape();
        http = new AHttp();
        image = new AImage(config.imageLoaderConfig);
        return this;
    }

    private void checkConfig(AUtilConfig utilConfig, ImageLoaderConfig imageLoaderConfig) {
        if (null == utilConfig) {
            throw new RuntimeException(
                    "The config of AVast is Null, please call the init(AUtilConfig config) method to initialize");
        }
        if (null == imageLoaderConfig) {
            throw new RuntimeException(
                    "The config of AImage is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }
    }

    /** 得到 AVast */
    public static AVast obtain() {
        if (null == aVast) {
            synchronized (AVast.class) {
                if (null == aVast) {
                    aVast = new AVast();
                }
            }
        }
        return aVast;
    }

    public void clearCache() {
        image.clearCache();
    }

}
