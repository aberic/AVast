package cn.aberic.avast.http.core;

import android.graphics.Bitmap;
import android.os.Process;
import android.support.annotation.Nullable;

import java.io.File;
import java.net.HttpURLConnection;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;
import cn.aberic.avast.http.net.HttpBitmapRequest;
import cn.aberic.avast.http.net.HttpFileRequest;
import cn.aberic.avast.http.net.HttpRequest;
import cn.aberic.avast.http.net.HttpStringRequest;
import cn.aberic.avast.http.requests.BitmapRequest;
import cn.aberic.avast.http.requests.FileRequest;
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
    private HttpFileRequest fileHttpRequest;
    /** 结果分发器,将结果投递到主线程 */
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();

    public AHttp() {
        stringHttpRequest = new HttpStringRequest();
        bitmapHttpRequest = new HttpBitmapRequest();
        fileHttpRequest = new HttpFileRequest();
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
                /// 设置线程的优先级
                Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY);
                stringRequest(Request.HttpMethod.POST, url, params, null, new Request.RequestListener<String>() {
                    @Override
                    public void onComplete(int stCode, String response) {
                        switch (stCode) {
                            case HttpURLConnection.HTTP_OK:
                                httpCallback.onSuccess(response);
                                break;
                            default:
                                httpCallback.onFailure(stCode, response);
                                break;
                        }
                    }
                });
            }
        });
    }

    public void get(final String url, final HttpCallback httpCallback) {
        AVast.obtain().threadPool.submitFixed(new Runnable() {
            @Override
            public void run() {
                /// 设置线程的优先级
                Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY);
                stringRequest(Request.HttpMethod.GET, url, null, null, new Request.RequestListener<String>() {
                    @Override
                    public void onComplete(int stCode, String response) {
                        switch (stCode) {
                            case HttpURLConnection.HTTP_OK:
                                httpCallback.onSuccess(response);
                                break;
                            default:
                                httpCallback.onFailure(stCode, response);
                                break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 异步加载图片
     *
     * @param url
     *         图片 url
     * @param requestListener
     *         结果回调
     */
    public void imageLoader(final String url, final Request.RequestListener<Bitmap> requestListener) {
        AVast.obtain().threadPool.submitFixed(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                bitmapRequest(url, requestListener);
            }
        });
    }

    /**
     * 异步下载附件
     *
     * @param url
     *         附件 url
     * @param fileName
     *         文件名
     * @param fileType
     *         文件类型(后缀名)
     * @param requestListener
     *         结果回调
     * @param loadListener
     *         进度回调
     */
    public void download(final String url, final String fileName, final String fileType, final Request.RequestListener<File> requestListener, final Request.LoadListener loadListener) {
        AVast.obtain().threadPool.submitFixed(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
                fileRequest(url, fileName, fileType, requestListener, loadListener);
            }
        });
    }

    private void stringRequest(Request.HttpMethod method, final String url, RequestParams params, @Nullable RequestFiles files, Request.RequestListener<String> requestListener) {
        StringRequest stringRequest = new StringRequest(method, url, requestListener);
        stringRequest.setParams(params);
        stringRequest.setFiles(files);
        Response response = stringHttpRequest.performRequest(stringRequest, null);
        mResponseDelivery.deliveryResponse(stringRequest, response);
    }

    private void bitmapRequest(final String url, Request.RequestListener<Bitmap> requestListener) {
        BitmapRequest bitmapRequest = new BitmapRequest(Request.HttpMethod.GET, url, requestListener);
        Response response = bitmapHttpRequest.performRequest(bitmapRequest, null);
        mResponseDelivery.deliveryResponse(bitmapRequest, response);
    }

    private void fileRequest(final String url, String fileName, String fileType, Request.RequestListener<File> requestListener, Request.LoadListener loadListener) {
        FileRequest fileRequest = new FileRequest(Request.HttpMethod.GET, url, requestListener);
        fileHttpRequest.setFileName(fileName);
        fileHttpRequest.setFileType(fileType);
        Response response = fileHttpRequest.performRequest(fileRequest, loadListener);
        mResponseDelivery.deliveryResponse(fileRequest, response);
    }


    public interface HttpCallback {
        void onFailure(int errorCode, String result);

        void onSuccess(String result);
    }

}
