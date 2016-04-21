package cn.aberic.avast.http.base;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.requests.RequestFiles;
import cn.aberic.avast.http.requests.RequestParams;
import cn.aberic.avast.cache.request.StringCacheRequest;

/**
 * Request是抽象，而不是具体。
 * <p/>
 * 而对于网络请求来说，用户得到的请求结果格式是不确定，比如有的服务器返回的是json,有的返回的是xml，有的直接是字符串。
 * <p/>
 * 但是对于Http Response来说，它的返回数据类型都是Stream，也就是我们得到的原始数据都是二进制的流。
 * <p/>
 * 所以在Request基类中我们必须预留方法来解析Response返回的具体类型，虽然返回的类型不同，但是他们的处理逻辑是一样的，
 * 因此我们可把Request作为泛型类，它的泛型类型就是它的返回数据类型，比如Request<String>，那么它的返回数据类型就是String类型的。
 * <p/>
 * 另外还有请求的优先级、可取消等.
 * <p/>
 * 网络请求类. 注意GET和DELETE不能传递请求参数,因为其请求的性质所致,可以将参数构建到url后传递进来到Request中
 * <p/>
 * 作者：Aberic on 16/2/14 21:07
 * 邮箱：abericyang@gmail.com
 *
 * @param <T>
 *         T为请求返回的数据类型
 */
public abstract class Request<T> {

    /** 连接没有网络或网络不可用 */
    public static final int REQUEST_WITH_NO_NET = -1;

    /** http请求方法枚举,这里我们只有GET, POST, PUT, DELETE四种 **/
    public enum HttpMethod {
        GET("GET"),
        POST("POST");

        /** http 请求类型 */
        private String mHttpMethod = "";

        HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    /** 默认连接请求类型 */
    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    /**
     * POST或PUT请求默认编码参数
     * {@link #getDefaultParamsEncoding()}
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    /** 请求 Listener 监听接口 */
    protected RequestListener<T> mRequestListener;
    /** 请求 url */
    private String mUrl = "";
    /** 请求的方法 */
    HttpMethod mHttpMethod = HttpMethod.GET;
    /** 请求的 header */
    private HashMap<String, String> mHeaders = new HashMap<>();
    /** 请求参数 */
    private RequestParams mParams = new RequestParams();
    /** 请求附件 */
    private RequestFiles mFiles = new RequestFiles();
    /** 字符串缓存 */
    private static final BaseCache<String> stringCache = AVast.obtain().cache.stringCache;
    /** 是否缓存结果 */
    protected boolean isCache = false;

    /**
     * 请求构造函数
     *
     * @param method
     *         http请求方法
     * @param url
     *         http请求url
     * @param listener
     *         http请求监听
     */
    public Request(HttpMethod method, String url, RequestListener<T> listener) {
        mHttpMethod = method;
        mUrl = url;
        mRequestListener = listener;
        isCache = true;
    }

    /**
     * 从原生的网络请求中解析结果,子类覆写
     *
     * @param response
     *         请求结果
     *
     * @return 解析后的请求结果
     */
    public abstract T parseResponse(Response response);

    /**
     * 处理 Response,该方法将运行在UI线程
     *
     * @param response
     *         待处理 response
     */
    public final void deliveryResponse(Response response) {
        // 解析得到结果
        T result = parseResponse(response);
        if (null != mRequestListener) {
            mRequestListener.onComplete(null != response ? response.getStatusCode() : REQUEST_WITH_NO_NET, result);
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public static String getDefaultParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getDefaultParamsEncoding();
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void addHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    public RequestParams getParams() {
        return mParams;
    }

    public void setParams(RequestParams params) {
        mParams = params;
    }

    public RequestFiles getFile() {
        return mFiles;
    }

    public void setFiles(RequestFiles files) {
        mFiles = files;
    }

    public boolean isHttps() {
        return mUrl.startsWith("https");
    }

    /**
     * 返回 POST 或者 PUT 请求时的参数字节数组
     *
     * @return 字节数组
     */
    public byte[] getParamsBody() {
        RequestParams params = getParams();
        if (null != params && params.get().size() > 0) {
            return encodeParams(params.get(), getDefaultParamsEncoding());
        }
        return null;
    }

    /**
     * 发送待上传文件
     *
     * @param outStream
     *         数据输出流
     */
    public void dealFilesBody(DataOutputStream outStream) {
        RequestFiles files = getFile();
        if (files != null && files.get().size() > 0) {
            dealFiles(outStream, files.get());
        }
    }

    /**
     * 将参数转换为URL指定编码参数
     *
     * @param params
     *         参数集
     * @param paramsEncoding
     *         编码
     *
     * @return 参数字节数组
     */
    private byte[] encodeParams(HashMap<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append("&");
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported : " + paramsEncoding, e);
        }
    }

    /**
     * 处理待上传文件
     *
     * @param outStream
     *         数据输出流
     * @param files
     *         参数集
     */
    private void dealFiles(DataOutputStream outStream, HashMap<String, File> files) {
        if (files != null) {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String CHARSET = "UTF-8";
            try {
                for (HashMap.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getKey()).append("\"").append(LINEND);
                    sb.append("Content-Type: application/octet-stream; charset=").append(CHARSET).append(LINEND);
                    sb.append(LINEND);
                    outStream.write(sb.toString().getBytes());
                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存
     *
     * @param request
     *         请求
     * @param result
     *         请求结果
     */
    protected void cacheResult(StringCacheRequest request, String result) {
        if (null != result && null != stringCache) {
            synchronized (stringCache) {
                stringCache.put(request, result);
            }
        }
    }

    /**
     * 网络请求 Listener ,会被执行在UI线程
     *
     * @param <T>
     *         请求的 response 类型
     */
    public interface RequestListener<T> {
        /**
         * 请求完成回调接口
         *
         * @param stCode
         *         请求结果码
         * @param response
         *         请求类型
         */
        void onComplete(int stCode, T response);
    }

    /** 下载任务时,监听下载进度 */
    public interface LoadListener {

        /**
         * 得到下载进度
         *
         * @param total
         *         下载总量(单位字节)
         * @param loaded
         *         下载当前量(单位字节)
         */
        void load(int total, int loaded);
    }

}
