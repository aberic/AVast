package cn.aberic.avast.http.config;

/**
 * http配置类
 * 作者：Aberic on 16/2/15 14:41
 * 邮箱：abericyang@gmail.com
 */
public abstract class HttpConfig {

    /** 用户请求代理 */
    public String userAgent = "default";
    /** 对应 Socket 超时毫秒数 */
    public int soTimeOut = 10000;
    /** 对应连接超时毫秒数 */
    public int connTimeOut = 10000;

}
