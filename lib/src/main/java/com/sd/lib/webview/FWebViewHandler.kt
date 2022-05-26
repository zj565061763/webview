package com.sd.lib.webview

import android.webkit.WebView
import java.net.HttpCookie

abstract class FWebViewHandler {
    /**
     * 初始化webview
     */
    abstract fun initWebView(webView: WebView)

    /**
     * 返回http的cookie
     */
    abstract fun getHttpCookie(url: String): List<HttpCookie>?

    /**
     * 把webview的cookie同步到http
     */
    abstract fun synchronizeWebViewCookieToHttp(url: String, listCookie: List<HttpCookie>)
}