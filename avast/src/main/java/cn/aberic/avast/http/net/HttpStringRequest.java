package cn.aberic.avast.http.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 14:38
 * 邮箱：abericyang@gmail.com
 */
public class HttpStringRequest extends HttpBaseRequest {

    /**
     * 获取请求结果(根据 HttpURLConnection 返回 Response 相应)
     *
     * @param conn
     *         请求连接
     *
     * @return 请求相应
     */
    @Override
    protected Response fetchResponse(HttpURLConnection conn, Request.LoadListener loadListener) throws IOException {
        Response response = new Response();
        response.setStatusCode(conn.getResponseCode());
        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {// 连接成功才处理数据

            StringBuilder sb = new StringBuilder();
            String readLine;
            BufferedReader responseReader;
            GZIPInputStream gis = null;

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String encoding = conn.getContentEncoding();// 默认已经设置 gzip 的请求头,此处获取 Content-Encoding 响应码
            InputStreamReader isr;
            if (null != encoding && !"".equals(encoding) && "gzip".equals(encoding)) {// 判断是否支持gzip压缩格式
                gis = new GZIPInputStream(bis);
                isr = new InputStreamReader(gis, "UTF-8");
            } else {
                isr = new InputStreamReader(bis, "UTF-8");
            }
            // 处理响应流，必须与服务器响应流输出的编码一致
            responseReader = new BufferedReader(isr);
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }

            response.setResult(sb.toString());
            AVast.obtain().util.method.closeQuietly(responseReader);
            AVast.obtain().util.method.closeQuietly(isr);
            AVast.obtain().util.method.closeQuietly(bis);
            AVast.obtain().util.method.closeQuietly(gis);
        } else {
            response.setResult("");
        }

        return response;
    }

}
