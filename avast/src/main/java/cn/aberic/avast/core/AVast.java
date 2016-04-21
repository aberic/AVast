package cn.aberic.avast.core;

import cn.aberic.avast.cache.core.ACache;
import cn.aberic.avast.http.core.AHttp;
import cn.aberic.avast.imageLoader.config.ImageLoaderConfig;
import cn.aberic.avast.imageLoader.core.AImage;
import cn.aberic.avast.pool.AThreadPool;
import cn.aberic.avast.shape.AShape;
import cn.aberic.avast.vast.AUtil;

/**
 * AVast 框架总入口
 * 作者：Aberic on 16/2/18 09:44
 * 邮箱：abericyang@gmail.com
 */
public class AVast {

    /** 缓存 操作核心 */
    public ACache cache;
    /** Http 操作核心 */
    public AHttp http;
    /** ImageLoader 操作核心 */
    public AImage image;
    /** 线程池操作核心 */
    public AThreadPool threadPool;
    /** 一般公共方法操作核心 */
    public AUtil util;
    /** Bitmap 形状操作核心 */
    public AShape shape;

    public AVast init(final AVastConfig config) {
        threadPool = new AThreadPool();// 必须实例化
        checkConfig(config.imageLoaderConfig);
        util = new AUtil(config.mContext);
        cache = new ACache(config.mContext);
        shape = new AShape();
        http = new AHttp();
        image = new AImage(config.imageLoaderConfig);
        return this;
    }

    private void checkConfig(ImageLoaderConfig imageLoaderConfig) {
        if (null == imageLoaderConfig) {
            throw new RuntimeException(
                    "The config of AImage is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }
    }

    private static AVast aVast;

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

    /** 清理内存空间 */
    public void clearMemoryCache() {
        cache.clearMemoryCache();
    }

    /** 清理磁盘空间 */
    public void clearDiskCache() {
        cache.clearDiskCache();
    }

}
