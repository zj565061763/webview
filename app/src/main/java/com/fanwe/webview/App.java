package com.fanwe.webview;

import android.app.Application;
import android.util.Log;
import android.webkit.WebView;

import com.fanwe.lib.webview.FWebViewHandler;
import com.fanwe.lib.webview.FWebViewManager;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Administrator on 2018/2/7.
 */

public class App extends Application
{
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();
        FWebViewManager.getInstance().setWebViewHandler(new FWebViewHandler()
        {
            @Override
            public void onInitWebView(WebView webView)
            {
                Log.i(TAG, "onInitWebView:" + webView);
            }

            @Override
            public List<HttpCookie> getHttpCookieForUrl(String url)
            {
                Log.i(TAG, "getHttpCookieForUrl:" + url);
                return null;
            }
        });
    }
}
