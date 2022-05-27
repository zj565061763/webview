package com.sd.lib.webview.client

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sd.lib.webview.R
import java.util.concurrent.CopyOnWriteArrayList

open class FWebViewClient(context: Context) : WebViewClient() {
    private val _activity: Activity
    private val _listActionView = CopyOnWriteArrayList<String>()
    private val _listBrowsable = CopyOnWriteArrayList<String>()

    //---------- Override start ----------

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url)
            return true
        }
        return interceptUrl(url)
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.proceed()
    }

    //---------- Override end ----------

    fun addActionViewUrl(url: String) {
        if (url.isEmpty()) return
        if (!_listActionView.contains(url)) {
            _listActionView.add(url)
        }
    }

    fun addBrowsableUrl(url: String) {
        if (url.isEmpty()) return
        if (!_listBrowsable.contains(url)) {
            _listBrowsable.add(url)
        }
    }

    /**
     * 是否拦截Url
     */
    protected open fun interceptUrl(url: String): Boolean {
        for (item in _listActionView) {
            if (url.startsWith(item)) {
                startActionViewUrl(url)
                return true
            }
        }
        for (item in _listBrowsable) {
            if (url.startsWith(item)) {
                startBrowsableUrl(url)
                return true
            }
        }
        return false
    }

    protected open fun startActionViewUrl(url: String) {
        try {
            val intent = Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = Uri.parse(url)
            }
            _activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun startBrowsableUrl(url: String) {
        try {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME).apply {
                this.addCategory(Intent.CATEGORY_BROWSABLE)
                this.component = null
            }
            _activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        require(context is Activity) { "context must be instance of " + Activity::class.java }
        this._activity = context

        val actionViewArray =
            _activity.resources.getStringArray(R.array.lib_webview_arr_action_view_url)
        for (item in actionViewArray) {
            addActionViewUrl(item)
        }

        val browsableArray =
            _activity.resources.getStringArray(R.array.lib_webview_arr_browsable_url)
        for (item in browsableArray) {
            addBrowsableUrl(item)
        }
    }
}