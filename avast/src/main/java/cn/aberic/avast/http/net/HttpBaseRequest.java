package cn.aberic.avast.http.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
    public Response performRequest(Request<?> request) {
        HttpURLConnection conn = null;
        try {
            conn = createHttpURLConnection(request.getUrl());// 构建 HttpURLConnection
            setRequestHeaders(conn, request);// 设置 headers
            setRequestParams(conn, request);// // 设置 params 参数
            configHttps(request);// https 配置
            return fetchResponse(conn);
        } catch (IOException e) {
            e.printStackTrace();
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
     * 设置连接请求参数集
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
        if (null != body) {
            conn.setDoOutput(true);// 允许输出
            conn.addRequestProperty(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(body);

            HashMap<String, File> files = null != request.getFile() ? request.getFile().get() : null;
            // 发送文件数据
            if (files != null) {
                String BOUNDARY = java.util.UUID.randomUUID().toString();
                String PREFIX = "--", LINEND = "\r\n";
                String CHARSET = "UTF-8";
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
            }

            outStream.flush();
            outStream.close();
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
    protected abstract Response fetchResponse(HttpURLConnection conn) throws IOException;

}
