package cn.aberic.avast.http.requests;

import android.graphics.Bitmap;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 11:20
 * 邮箱：abericyang@gmail.com
 */
public class BitmapRequest extends Request<Bitmap> {

    /**
     * 请求构造函数,返回数据类型为 Bitmap 的请求
     *
     * @param method
     *         http请求方法
     * @param url
     *         http请求url
     * @param listener
     *         http请求监听
     */
    public BitmapRequest(HttpMethod method, String url, RequestListener<Bitmap> listener) {
        super(method, url, listener);
    }

    @Override
    public Bitmap parseResponse(Response response) {
        if (null == response) {
            response = new Response();
            response.setStatusCode(REQUEST_WITH_NO_NET);
            response.setBitmap(null);
        }
        return response.getBitmap();
    }
}
