package com.sd.lib.webview;

import android.webkit.WebView;

import com.sd.lib.webview.cookie.FWebViewCookie;

import java.net.HttpCookie;
import java.util.List;

public abstract class FWebViewHandler
{
    public static final FWebViewHandler DEFAULT = new FWebViewHandler()
    {
        @Override
        public void onInitWebView(WebView webView)
        {
        }

        @Override
        public List<HttpCookie> getHttpCookieForUrl(String url)
        {
            return null;
        }

        @Override
        public void synchronizeWebViewCookieToHttp(String url, List<HttpCookie> listCookie)
        {
        }
    };

    /**
     * 初始化webview
     *
     * @param webView
     */
    public abstract void onInitWebView(WebView webView);

    //---------- http to webview ----------

    /**
     * 返回url对应的http的cookie
     *
     * @param url
     * @return
     */
    public abstract List<HttpCookie> getHttpCookieForUrl(String url);

    /**
     * 同步url对应的http的cookie到webview
     *
     * @param url
     */
    public void synchronizeHttpCookieToWebView(String url)
    {
        final List<HttpCookie> listHttpCookie = getHttpCookieForUrl(url);
        FWebViewCookie.setCookie(url, listHttpCookie);
    }

    //---------- webview to http ----------

    /**
     * 返回url对应的webview的cookie
     *
     * @param url
     * @return
     */
    public List<HttpCookie> getWebViewCookieForUrl(String url)
    {
        final List<HttpCookie> listCookie = FWebViewCookie.getCookieAsList(url);
        return listCookie;
    }

    /**
     * 同步url对应的webview的cookie到http
     *
     * @param url
     * @param listCookie
     */
    public abstract void synchronizeWebViewCookieToHttp(String url, List<HttpCookie> listCookie);
}
