package cn.aberic.avast.http.net;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;
import cn.aberic.avast.http.config.HttpUrlConnConfig;

/**
 * 作者：Aberic on 16/2/15 14:38
 * 邮箱：abericyang@gmail.com
 */
public abstract class HttpBaseRequest implements HttpRequest {

    @Override
    public Response performRequest(Request<?> request, Request.LoadListener loadListener) {
        HttpURLConnection conn = null;
        try {
            conn = createHttpURLConnection(request.getUrl());// 构建 HttpURLConnection
            setRequestHeaders(conn, request);// 设置 headers
            setRequestParams(conn, request);// // 设置 params 参数
            configHttps(request);// https 配置
            return fetchResponse(conn, loadListener);
        } catch (IOException e) {
            Log.w("HttpBaseRequest", "网络连接失败,IOException = " + e.getMessage());
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
        return null;
    }

    /** 配置 Https */
    HttpUrlConnConfig mConfig = HttpUrlConnConfig.getConfig();

    /**
     * 获取并配置 HttpURLConnection
     *
     * @param url
     *         url请求地址
     *
     * @return HttpURLConnection
     *
     * @throws IOException
     */
    private HttpURLConnection createHttpURLConnection(String url) throws IOException {
        URL newUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();// 获得链接
        conn.setConnectTimeout(mConfig.connTimeOut);// 对应connection timeout
        conn.setReadTimeout(mConfig.soTimeOut);// 对应Socket timeout
        conn.setDoInput(true);// 允许输入
        /* 为了性能更好，应该设置setFixedLengthStreamingMod(int)或者setChunkedStramingMode(int)。
         * 如果不设置，request将的带buffer已经完成body的写，再发送，这对body数据量大的情况下显然效率较低. */
        conn.setChunkedStreamingMode(0);
        return conn;
    }

    /**
     * 设置连接请求属性(HTTP 请求头部信息)
     *
     * @param conn
     *         请求连接
     * @param request
     *         请求
     */
    private void setRequestHeaders(HttpURLConnection conn, Request<?> request) {
        Set<String> headersKeys = request.getHeaders().keySet();
        for (String headerName : headersKeys) {
            conn.setRequestProperty(headerName, request.getHeaders().get(headerName));
        }
        conn.setRequestProperty("Accept-Encoding", "gzip");// 需要设置 gzip的请求头 才可以获取Content-Encoding响应码
    }

    /**
     * 设置并处理连接请求参数集
     *
     * @param conn
     *         请求连接
     * @param request
     *         请求
     *
     * @throws IOException
     */
    private void setRequestParams(HttpURLConnection conn, Request<?> request) throws IOException {
        Request.HttpMethod method = request.getHttpMethod();// 获取请求方法
        conn.setRequestMethod(method.toString());// 设置连接请求方法
        // 添加参数信息
        byte[] body = request.getParamsBody();
        if (null != body) { // 参数是否为空
            conn.setDoOutput(true);// 当参数不为空时才允许输出
            conn.addRequestProperty(Request.HEADER_CONTENT_TYPE, request.getBodyContentType()); // 当需要传递参数时,方设置头部信息
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream()); // 写出数据流方式
            outStream.write(body); // 写出参数信息
            request.dealFilesBody(outStream); // 如果存在附件,则写出附件信息
            outStream.flush(); // 强制刷新写出
            outStream.close(); // 关闭输出流
        }
    }

    /**
     * 根据请求配置 Https
     *
     * @param request
     *         请求
     */
    private void configHttps(Request<?> request) {
        if (request.isHttps()) {
            SSLSocketFactory sslSocketFactory = mConfig.getSSlSocketFactory();
            // 配置 Https
            if (null != sslSocketFactory) {
                HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
                HttpsURLConnection.setDefaultHostnameVerifier(mConfig.getHostnameVerifier());
            }
        }
    }

    /**
     * 获取请求结果(根据 HttpURLConnection 返回 Response 相应)
     *
     * @param conn
     *         请求连接
     *
     * @return 请求相应
     */
    protected abstract Response fetchResponse(HttpURLConnection conn, Request.LoadListener loadListener) throws IOException;

}
