package cn.aberic.avast.http.base;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 作者：Aberic on 16/2/14 21:32
 * 邮箱：abericyang@gmail.com
 */
public class Response {

    /** 请求返回状态码 */
    private int statusCode;
    /** 请求返回字符串 */
    private String result;
    /** 请求返回附件转 Bitmap 类 */
    private Bitmap bitmap;
    /** 请求返回附件转 File 类 */
    private File file;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
