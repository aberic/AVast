package cn.aberic.avast.http.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import cn.aberic.avast.cache.impl.BitmapDiskCache;
import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 14:38
 * 邮箱：abericyang@gmail.com
 */
public class HttpBitmapRequest extends HttpBaseRequest {

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

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), BitmapDiskCache.IO_BUFFER_SIZE);
        Bitmap bitmap = BitmapFactory.decodeStream(bis);

        response.setBitmap(bitmap);
        AVast.obtain().util.method.closeQuietly(bis);

        return response;
    }

}
