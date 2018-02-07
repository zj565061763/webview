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

    private Callback mCallback;

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

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    private Callback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new Callback()
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
            };
        }
        return mCallback;
    }

    public void notifyWebViewInit(WebView webView)
    {
        getCallback().onInitWebView(webView);
    }

    /**
     * 把url对应的http的cookie同步到webview
     *
     * @param url
     */
    public void synchronizeHttpCookieToWebView(String url)
    {
        List<HttpCookie> listHttpCookie = getCallback().getHttpCookieForUrl(url);
        FWebViewCookie.setCookie(url, listHttpCookie);
    }

    public interface Callback
    {
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
    }
}
