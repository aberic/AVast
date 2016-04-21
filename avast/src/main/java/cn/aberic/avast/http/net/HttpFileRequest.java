package cn.aberic.avast.http.net;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 14:38
 * 邮箱：abericyang@gmail.com
 */
public class HttpFileRequest extends HttpBaseRequest {

    /** 文件名 */
    private String fileName;
    /** 文件类型(后缀名) */
    private String fileType;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

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

        InputStream is = conn.getInputStream();
        AVast.obtain().util.file.createFileDir("download"); // 创建下载文件夹
        if (null == fileType) {
            throw new RuntimeException("HttpFileRequest : file type is not assign !");
        }
        File file = new File(AVast.obtain().util.file.getFileDir("download"), null != fileName ? fileName : "financial_" + new Date().getTime() + "." + fileType); // 获取下载目录 File
        Log.d("HttpFileRequest", "file = " + file.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int total = conn.getContentLength();
        int len;
        int loaded = 0; //  当前写入长度
        long first = new Date().getTime(); // 首次加载时间戳
        long now; // 当前加载时间戳
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            loaded += len; // 更新总长度
            now = new Date().getTime(); // 获取当前时间戳
            if (now - first > 500) { // 如果间隔超过 500ms 则更新一次进度
                // 获取当前下载量
                loadListener.load(total, loaded);
                first = now;
            }
        }
        AVast.obtain().util.method.closeQuietly(fos);
        AVast.obtain().util.method.closeQuietly(bis);
        AVast.obtain().util.method.closeQuietly(is);

        response.setFile(file);
        return response;
    }

}
