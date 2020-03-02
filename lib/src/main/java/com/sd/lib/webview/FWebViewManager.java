package com.sd.lib.webview;

import android.text.TextUtils;
import android.webkit.WebView;

import java.net.HttpCookie;
import java.util.List;

public class FWebViewManager
{
    private static FWebViewManager sInstance;

    private FWebViewHandler mWebViewHandler;

    private FWebViewManager()
    {
    }

    public static FWebViewManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (FWebViewManager.class)
            {
                if (sInstance == null)
                    sInstance = new FWebViewManager();
            }
        }
        return sInstance;
    }

    public void setWebViewHandler(FWebViewHandler webViewHandler)
    {
        mWebViewHandler = webViewHandler;
    }

    private FWebViewHandler getWebViewHandler()
    {
        if (mWebViewHandler == null)
            mWebViewHandler = FWebViewHandler.DEFAULT;
        return mWebViewHandler;
    }

    public void notifyInitWebView(WebView webView)
    {
        getWebViewHandler().onInitWebView(webView);
    }

    /**
     * 把url对应的http的cookie同步到webview
     *
     * @param url
     */
    public void synchronizeHttpCookieToWebView(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        getWebViewHandler().synchronizeHttpCookieToWebView(url);
    }

    /**
     * 同步url对应的webview的cookie到http
     *
     * @param url
     */
    public void synchronizeWebViewCookieToHttp(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        final List<HttpCookie> listCookie = getWebViewHandler().getWebViewCookieForUrl(url);
        getWebViewHandler().synchronizeWebViewCookieToHttp(url, listCookie);
    }
}
