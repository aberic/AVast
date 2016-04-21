package cn.aberic.avast.http.response;

import cn.aberic.avast.http.base.Response;

/**
 * User: Aberic Yang(abericyang@gmail.com)
 * Date: 2016-04-11
 * Time: 10:35
 */
public class StringResponse extends Response{

    /** 请求返回字符串 */
    private String result;
    /** 请求返回附件转 Bitmap 类 */

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
