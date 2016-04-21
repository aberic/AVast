package cn.aberic.avast.http.requests;

import java.io.File;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 作者：Aberic on 16/2/15 11:20
 * 邮箱：abericyang@gmail.com
 */
public class FileRequest extends Request<File> {

    /**
     * 请求构造函数,返回数据类型为 File 的请求
     *
     * @param method
     *         http请求方法
     * @param url
     *         http请求url
     * @param listener
     *         http请求监听
     */
    public FileRequest(HttpMethod method, String url, RequestListener<File> listener) {
        super(method, url, listener);
    }

    @Override
    public File parseResponse(Response response) {
        if (null == response) {
            response = new Response();
            response.setStatusCode(REQUEST_WITH_NO_NET);
            response.setFile(null);
        }
        return response.getFile();
    }
}
