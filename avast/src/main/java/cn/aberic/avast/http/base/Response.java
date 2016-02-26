package cn.aberic.avast.http.base;

import android.graphics.Bitmap;

/**
 * 作者：Aberic on 16/2/14 21:32
 * 邮箱：abericyang@gmail.com
 */
public class Response {

    private int statusCode;
    private String result;
    private Bitmap bitmap;

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
}
