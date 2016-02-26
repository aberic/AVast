package cn.aberic.avast.imageLoader.loader;

import cn.aberic.avast.imageLoader.request.BitmapRequest;

/**
 * 图片加载接口
 * 作者：Aberic on 16/2/16 23:22
 * 邮箱：abericyang@gmail.com
 */
public interface Loader {
    /**
     * 加载图片
     *
     * @param request
     *         图片请求
     */
    void loadImage(BitmapRequest request);
}
