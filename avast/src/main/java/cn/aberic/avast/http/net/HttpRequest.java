package cn.aberic.avast.http.net;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * HttpStack 只是一个接口，只有一个 performRequest 函数，也就是执行请求
 * <p/>
 * 执行网络请求的接口
 * 作者：Aberic on 16/2/15 11:46
 * 邮箱：abericyang@gmail.com
 */
public interface HttpRequest {

    /**
     * 执行Http请求
     *
     * @param request
     *         待执行的请求
     * @param loadListener
     *         下载任务时,监听下载进度
     *
     * @return 请求结果
     */
    Response performRequest(Request<?> request, Request.LoadListener loadListener);

}
