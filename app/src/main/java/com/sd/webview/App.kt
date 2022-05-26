package com.sd.webview

import android.app.Application
import android.util.Log
import android.webkit.WebView
import com.sd.lib.webview.FWebViewHandler
import com.sd.lib.webview.FWebViewManager
import java.net.HttpCookie

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FWebViewManager.setWebViewHandler(_webViewHandler) //设置WebViewHandler
    }

    private val _webViewHandler = object : FWebViewHandler() {
        override fun initWebView(webView: WebView) {
            Log.i(TAG, "initWebView:$webView")
        }

        override fun getHttpCookie(url: String): List<HttpCookie>? {
            Log.i(TAG, "getHttpCookie:$url")
            return null
        }

        override fun synchronizeWebViewCookieToHttp(url: String, listCookie: List<HttpCookie>) {
            Log.i(TAG, "synchronizeWebViewCookieToHttp url:$url cookie:$listCookie")
        }
    }

    companion object {
        const val TAG = "webview_demo"
    }
}