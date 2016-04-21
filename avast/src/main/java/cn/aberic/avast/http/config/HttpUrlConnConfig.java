package cn.aberic.avast.http.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * 这是针对于使用 HttpUrlStack 执行请求时为 https 请求设置的 SSLSocketFactory 和 HostnameVerifier 的配置类
 * 作者：Aberic on 16/2/15 14:44
 * 邮箱：abericyang@gmail.com
 */
public class HttpUrlConnConfig extends HttpConfig {


    private static HttpUrlConnConfig sConfig = new HttpUrlConnConfig();

    private SSLSocketFactory mSSlSocketFactory = null;
    private HostnameVerifier mHostnameVerifier = null;

    private HttpUrlConnConfig() {
    }

    public static HttpUrlConnConfig getConfig() {
        return sConfig;
    }

    /**
     * 配置https请求的SSLSocketFactory与HostnameVerifier
     *
     * @param sslSocketFactory
     *         SSLSocketFactory
     * @param hostnameVerifier
     *         HostnameVerifier
     */
    public void setHttpsConfig(SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier) {
        mSSlSocketFactory = sslSocketFactory;
        mHostnameVerifier = hostnameVerifier;
    }

    public SSLSocketFactory getSSlSocketFactory() {
        return mSSlSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }
}
