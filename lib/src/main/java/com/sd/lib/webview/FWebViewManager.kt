package com.sd.lib.webview

import android.webkit.WebView
import com.sd.lib.webview.cookie.FWebViewCookie

object FWebViewManager {
    private var _webViewHandler: FWebViewHandler? = null

    fun setWebViewHandler(webViewHandler: FWebViewHandler?) {
        _webViewHandler = webViewHandler
    }

    fun initWebView(webView: WebView) {
        _webViewHandler?.initWebView(webView)
    }

    /**
     * 把http的cookie同步到webview
     */
    fun synchronizeHttpCookieToWebView(url: String?) {
        if (url.isNullOrEmpty()) return
        _webViewHandler?.let { handler ->
            val listCookie = handler.getHttpCookie(url)
            FWebViewCookie.setCookie(url, listCookie)
        }
    }

    /**
     * 把webview的cookie同步到http
     */
    fun synchronizeWebViewCookieToHttp(url: String?) {
        if (url.isNullOrEmpty()) return
        _webViewHandler?.let { handler ->
            val listCookie = FWebViewCookie.getCookieAsList(url)
            if (listCookie.isNotEmpty()) {
                handler.synchronizeWebViewCookieToHttp(url, listCookie)
            }
        }
    }
}