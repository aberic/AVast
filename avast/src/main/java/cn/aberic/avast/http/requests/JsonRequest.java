package cn.aberic.avast.http.requests;

import org.json.JSONException;
import org.json.JSONObject;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/16 11:27
 * 邮箱：abericyang@gmail.com
 */
public class JsonRequest extends Request<JSONObject> {

    /**
     * 请求构造函数,返回数据类型为 JSON 的请求
     *
     * @param method
     *         http请求方法
     * @param url
     *         http请求url
     * @param listener
     *         http请求监听
     */
    public JsonRequest(HttpMethod method, String url, RequestListener<JSONObject> listener) {
        super(method, url, listener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        if (null == response) {
            response = new Response();
            response.setStatusCode(REQUEST_WITH_NO_NET);
            response.setResult("");
            return null;
        }
        try {
            return new JSONObject(response.getResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
