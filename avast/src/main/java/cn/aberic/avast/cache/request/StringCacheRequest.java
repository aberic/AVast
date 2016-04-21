package cn.aberic.avast.cache.request;

import cn.aberic.avast.core.AVast;

/**
 * 网络请求类.
 * 作者：Aberic on 16/2/16 21:22
 * 邮箱：abericyang@gmail.com
 */
public class StringCacheRequest implements CacheRequest {

    /** 图片加载路径 */
    public String uri = "";
    /** 图片缓存 key(url 可能会导致部分问题,因此转为md5形式作为 key 使用) */
    public String uriMd5 = "";

    public StringCacheRequest(String uri) {
        this.uri = uri;
        uriMd5 = AVast.obtain().util.md5.getMD5(this.uri);
    }

}