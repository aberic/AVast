package cn.aberic.avast.http.requests;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 11:20
 * 邮箱：abericyang@gmail.com
 */
public class StringRequest extends Request<String> {

    /**
     * 请求构造函数,返回数据类型为String的请求
     *
     * @param method
     *         http请求方法
     * @param url
     *         http请求url
     * @param listener
     *         http请求监听
     */
    public StringRequest(HttpMethod method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        if (null == response) {
            response = new Response();
            response.setStatusCode(REQUEST_WITH_NO_NET);
            response.setResult("");
        }
        return response.getResult();
    }
}
