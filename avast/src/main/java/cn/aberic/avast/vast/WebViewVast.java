package cn.aberic.avast.vast;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * WebView管理器，提供常用设置
 */
public class WebViewVast {
    private WebSettings webSettings;

    /**
     * 开启管理
     *
     * @param webView
     *         webView
     */
    public WebViewVast manager(WebView webView) {
        webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        return this;
    }

    /**
     * 开启自适应功能
     */
    public WebViewVast enableAdaptive() {
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 禁用自适应功能
     */
    public WebViewVast disableAdaptive() {
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 开启缩放功能
     */
    public WebViewVast enableZoom() {
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        return this;
    }

    /**
     * 禁用缩放功能
     */
    public WebViewVast disableZoom() {
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        return this;
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    public WebViewVast enableJavaScript() {
        webSettings.setJavaScriptEnabled(true);
        return this;
    }

    /**
     * 禁用JavaScript
     */
    public WebViewVast disableJavaScript() {
        webSettings.setJavaScriptEnabled(false);
        return this;
    }

    /**
     * 开启JavaScript自动弹窗
     */
    public WebViewVast enableJavaScriptOpenWindowsAutomatically() {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        return this;
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    public WebViewVast disableJavaScriptOpenWindowsAutomatically() {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        return this;
    }

    /** 开启缓存 */
    public WebViewVast enableCache() {
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        return this;
    }

    /** 禁止使用任何缓存(只从网络获取数据) */
    public WebViewVast enableNoCache() {
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        return this;
    }

    /**
     * 返回
     *
     * @return true：已经返回，false：到头了没法返回了
     */
    public boolean goBack(WebView mWebView) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }
}