package com.fanwe.lib.webview;

import android.webkit.WebView;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by zhengjun on 2018/2/7.
 */
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
                {
                    sInstance = new FWebViewManager();
                }
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
        {
            mWebViewHandler = FWebViewHandler.DEFAULT;
        }
        return mWebViewHandler;
    }

    public void notifyWebViewInit(WebView webView)
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
        List<HttpCookie> listHttpCookie = getWebViewHandler().getHttpCookieForUrl(url);
        FWebViewCookie.setCookie(url, listHttpCookie);
    }
}
