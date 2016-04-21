package cn.aberic.avast.http.response;

import android.graphics.Bitmap;

import cn.aberic.avast.http.base.Response;

/**
 * User: Aberic Yang(abericyang@gmail.com)
 * Date: 2016-04-11
 * Time: 10:35
 */
public class BitmapResponse extends Response{

    /** 请求返回附件转 Bitmap 类 */
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
