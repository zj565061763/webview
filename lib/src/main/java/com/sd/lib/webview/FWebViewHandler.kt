package com.sd.lib.webview;

import android.webkit.WebView;

import java.net.HttpCookie;
import java.util.List;

public abstract class FWebViewHandler {
    /**
     * 初始化webview
     */
    public abstract void onInitWebView(WebView webView);

    /**
     * 返回http的cookie
     */
    public abstract List<HttpCookie> getHttpCookie(String url);

    /**
     * 把webview的cookie同步到http
     */
    public abstract void synchronizeWebViewCookieToHttp(String url, List<HttpCookie> listCookie);
}
