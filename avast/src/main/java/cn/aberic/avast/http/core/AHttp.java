package cn.aberic.avast.http.core;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.net.HttpURLConnection;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;
import cn.aberic.avast.http.net.HttpBitmapRequest;
import cn.aberic.avast.http.net.HttpRequest;
import cn.aberic.avast.http.net.HttpStringRequest;
import cn.aberic.avast.http.requests.BitmapRequest;
import cn.aberic.avast.http.requests.RequestFiles;
import cn.aberic.avast.http.requests.RequestParams;
import cn.aberic.avast.http.requests.StringRequest;

/**
 * Http 框架入口
 * 作者：Aberic on 16/2/15 16:31
 * 邮箱：abericyang@gmail.com
 */
public class AHttp {

//    private static AHttp instance = null;
    private HttpRequest stringHttpRequest;
    private HttpRequest bitmapHttpRequest;
    /** 结果分发器,将结果投递到主线程 */
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();

    public AHttp() {
        stringHttpRequest = new HttpStringRequest();
        bitmapHttpRequest = new HttpBitmapRequest();
    }

//    public static AHttp getInstance() {
//        if (null == instance) {
//            synchronized (AHttp.class) {
//                if (null == instance) {
//                    instance = new AHttp();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * POST 方法获取网络数据
     *
     * @param url
     *         网络地址
     * @param params
     *         参数集
     * @param httpCallback
     *         结果回调
     */
    public void post(final String url, final RequestParams params, final HttpCallback httpCallback) {
        AVast.obtain().threadPool.submitFixed(new Runnable() {
            @Override
            public void run() {
                stringRequest(Request.HttpMethod.POST, url, params, null, new Request.RequestListener<String>() {
                    @Override
                    public void onComplete(int stCode, String response) {
                        switch (stCode) {
                            case HttpURLConnection.HTTP_OK:
                                httpCallback.onSuccess(response);
                                break;
                            default:
                                httpCallback.onFailure(stCode);
                                break;
                        }
                    }
                });
            }
        });
    }

    public void aImageLoader(final String url, final Request.RequestListener<Bitmap> listener) {
        AVast.obtain().threadPool.submitCached(new Runnable() {
            @Override
            public void run() {
                bitmapRequest(url, listener);
            }
        });
    }

    private void stringRequest(Request.HttpMethod method, final String url, RequestParams params, @Nullable RequestFiles files
            , Request.RequestListener<String> listener) {
        StringRequest stringRequest = new StringRequest(method, url, listener);
        stringRequest.setParams(params);
        stringRequest.setFiles(files);
        Response response = stringHttpRequest.performRequest(stringRequest);
        mResponseDelivery.deliveryResponse(stringRequest, response);
    }

    private void bitmapRequest(final String url, Request.RequestListener<Bitmap> listener) {
        BitmapRequest bitmapRequest = new BitmapRequest(Request.HttpMethod.GET, url, listener);
        Response response = bitmapHttpRequest.performRequest(bitmapRequest);
        mResponseDelivery.deliveryResponse(bitmapRequest, response);
    }


    public interface HttpCallback {
        void onFailure(int errorCode);

        void onSuccess(String result);
    }

}
