package cn.aberic.avast.util;

import android.content.Context;

import cn.aberic.avast.core.ConfigVast;

/**
 * 程序通用工具类集合
 * 作者：Aberic on 16/2/18 15:53
 * 邮箱：abericyang@gmail.com
 */
public class AUtilConfig implements ConfigVast {

    /** 图片压缩功能类 */
    public ImageCompressSize imageCompressSize;
    /** 与File相关方法 */
    public FileVast file;
    /** 一般日期（时间）处理通用公共方法类 */
    public DateVast date;
    /** MD5加密处理工具类 */
    public MD5Vast md5;
    /** SharedPreferences存储工具类 */
    public SharePfsVast sharePfs;
    /** Toast工具类，内置两个方法分别用于短时间和长时间来显示Toast */
    public ToastVast toast;
    /** 一般通用公共方法类 */
    public MethodVast method;
    /** 一般 Bitmap（图片）处理通用公共方法类 */
    public BitmapVast bitmap;
    /** 实现的Zip工具 */
    public ZipVast zip;
    /** SD卡工具箱 */
    public SDCardVast sdCard;
    /** WebView管理器，提供常用设置 */
    public WebViewVast webView;
    /** 正则验证相关操作 */
    public RegexVast regex;
    /** 网络工作工具类 */
    public NetWorkVast netWork;
    /** 软键盘工具类 */
    public InputVast input;
    /** 弹窗工具类 */
    public DialogVast dialog;
    public NotificationVast notification;

    @Override
    public AUtilConfig init(final Context context) {
        date = new SingletonVast<DateVast>() {
            @Override
            protected DateVast newInstance() {
                return new DateVast();
            }
        }.getInstance();
        file = new SingletonVast<FileVast>() {
            @Override
            protected FileVast newInstance() {
                return new FileVast();
            }
        }.getInstance();
        imageCompressSize = new ImageCompressSize();
        toast = new ToastVast(context);
        sharePfs = new SharePfsVast(context);
        method = new MethodVast(context);
        bitmap = new BitmapVast(context);
        zip = new SingletonVast<ZipVast>() {
            @Override
            protected ZipVast newInstance() {
                return new ZipVast();
            }
        }.getInstance();
        sdCard = new SingletonVast<SDCardVast>() {
            @Override
            protected SDCardVast newInstance() {
                return new SDCardVast();
            }
        }.getInstance();
        regex = new SingletonVast<RegexVast>() {
            @Override
            protected RegexVast newInstance() {
                return new RegexVast();
            }
        }.getInstance();
        webView = new SingletonVast<WebViewVast>() {
            @Override
            protected WebViewVast newInstance() {
                return new WebViewVast();
            }
        }.getInstance();
        netWork = new SingletonVast<NetWorkVast>() {
            @Override
            protected NetWorkVast newInstance() {
                return new NetWorkVast();
            }
        }.getInstance();
        md5 = new SingletonVast<MD5Vast>() {
            @Override
            protected MD5Vast newInstance() {
                return new MD5Vast();
            }
        }.getInstance();
        input = new SingletonVast<InputVast>() {
            @Override
            protected InputVast newInstance() {
                return new InputVast();
            }
        }.getInstance();
        dialog = new SingletonVast<DialogVast>() {
            @Override
            protected DialogVast newInstance() {
                return new DialogVast();
            }
        }.getInstance();
        notification = new SingletonVast<NotificationVast>() {
            @Override
            protected NotificationVast newInstance() {
                return new NotificationVast(context);
            }
        }.getInstance();
        return this;
    }

}
