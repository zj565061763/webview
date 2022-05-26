package com.sd.lib.webview.cookie

import android.webkit.CookieManager
import android.webkit.ValueCallback
import java.net.HttpCookie
import java.net.URI
import java.net.URISyntaxException

object FWebViewCookie {
    @JvmStatic
    fun getCookie(url: String?): String {
        return CookieManager.getInstance().getCookie(url) ?: ""
    }

    @JvmStatic
    fun getCookieAsList(url: String?): List<HttpCookie> {
        val cookie = getCookie(url)
        if (cookie.isEmpty()) {
            return listOf()
        }

        val listSplit = cookie.split(";")
        if (listSplit.isEmpty()) {
            return listOf()
        }

        val listResult = mutableListOf<HttpCookie>()
        for (item in listSplit) {
            val listPair = item.split("=")
            if (listPair.size == 2) {
                listResult.add(HttpCookie(listPair[0], listPair[1]))
            }
        }
        return listResult
    }

    //---------- set ----------
    @JvmStatic
    fun setCookie(url: String?, listCookie: List<HttpCookie>?): Boolean {
        if (listCookie.isNullOrEmpty()) return false
        val uri = toURI(url) ?: return false
        for (cookie in listCookie) {
            val cookieString = cookie.name + "=" + cookie.value
            if (!setCookieInternal(uri, cookieString)) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun setCookie(url: String?, cookie: HttpCookie?): Boolean {
        if (cookie == null) return false
        return setCookie(url, cookie.name + "=" + cookie.value)
    }

    @JvmStatic
    fun setCookie(url: String?, cookie: String?): Boolean {
        return setCookieInternal(toURI(url), cookie)
    }

    @JvmStatic
    private fun setCookieInternal(uri: URI?, cookie: String?): Boolean {
        if (uri == null || cookie.isNullOrEmpty()) return false
        val url = uri.scheme + "://" + uri.host
        CookieManager.getInstance().setCookie(url, cookie)
        return true
    }

    //---------- other ----------
    /**
     * [CookieManager.removeSessionCookies]
     */
    @JvmStatic
    fun removeSessionCookies(callback: ValueCallback<Boolean>) {
        CookieManager.getInstance().removeSessionCookies(callback)
    }

    /**
     * [CookieManager.removeAllCookies]
     */
    @JvmStatic
    fun removeAllCookie(callback: ValueCallback<Boolean>) {
        CookieManager.getInstance().removeAllCookies(callback)
    }

    /**
     * [CookieManager.flush]
     */
    @JvmStatic
    fun flush() {
        CookieManager.getInstance().flush()
    }

    @JvmStatic
    private fun toURI(url: String?): URI? {
        if (url.isNullOrEmpty()) return null
        return try {
            URI(url)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }
}