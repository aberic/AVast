package cn.aberic.avast.http.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import cn.aberic.avast.http.base.Request;
import cn.aberic.avast.http.base.Response;

/**
 * 不管是从缓存中获取还是从网络上获取，我们得到的都是一个Response对象，最后我们通过ResponseDelivery对象将结果分发给UI线程。
 * ResponseDelivery其实就是封装了关联了UI线程消息队列的Handler，在deliveryResponse函数中将request的deliveryResponse执行在UI线程中。
 * 既然我们有了关联了UI线程的Handler对象，那么直接构建一个Runnable，在该Runnable中执行request的deliveryResponse函数即可。
 * 在Request类的deliveryResponse中，又会调用parseResponse解析Response结果，返回的结果类型就是Request<T>中的T，这个T是在Request子类中指定，
 * 例如JsonRequest，那么返回的Response的结果就是JSONObject。这样我们就得到了服务器返回的json数据，并且将这个json结果通过回调的形式传递给了UI线程。
 * 用户就可以在该回调中更新UI了。
 * 这其中主要就是抽象和泛型，写框架很多时候泛型是很重要的手段，因此熟悉使用抽象和泛型是面向对象开发的重要一步。
 * <p/>
 * 请求结果投递类,将请求结果投递给UI线程
 * <p/>
 * 作者：Aberic on 16/2/15 11:49
 * 邮箱：abericyang@gmail.com
 */
class ResponseDelivery implements Executor {

    /** 主线程 handler */
    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    /**
     * 处理请求结果,将执行在 UI 线程
     *
     * @param request
     *         请求
     * @param response
     *         请求结果
     */
    public void deliveryResponse(final Request<?> request, final Response response) {
        Runnable respRunnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(respRunnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }
}
