package com.fanwe.lib.webview;

import android.webkit.WebView;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by zhengjun on 2018/2/7.
 */
public interface FWebViewHandler
{
    FWebViewHandler DEFAULT = new FWebViewHandler()
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
        public void synchronizeWebViewCookieToHttp(String cookie, List<HttpCookie> listCookie, String url)
        {
        }
    };

    /**
     * 初始化webview
     *
     * @param webView
     */
    void onInitWebView(WebView webView);

    /**
     * 返回url对应的http的cookie
     *
     * @param url
     * @return
     */
    List<HttpCookie> getHttpCookieForUrl(String url);

    /**
     * 同步url对应的webview的cookie到http
     *
     * @param cookie
     * @param listCookie
     * @param url
     */
    void synchronizeWebViewCookieToHttp(String cookie, List<HttpCookie> listCookie, String url);
}
