package com.fanwe.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.fanwe.lib.webview.FWebView;
import com.fanwe.lib.webview.FWebViewHandler;
import com.fanwe.lib.webview.FWebViewManager;
import com.fanwe.lib.webview.client.FWebChromeClient;
import com.fanwe.lib.webview.client.FWebViewClient;

import java.net.HttpCookie;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FWebView mWebView;
    private TextView mTvTitle;
    private String mUrl = "http://www.baidu.com";

    private FWebViewClient mWebViewClient;
    private FWebChromeClient mWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FWebViewManager.getInstance().setWebViewHandler(mWebViewHandler); //设置WebViewHandler

        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.webview);
        mTvTitle = findViewById(R.id.tv_title);

        mWebView.setWebViewClient(getWebViewClient());     //设置WebViewClient
        mWebView.setWebChromeClient(getWebChromeClient()); //设置WebChromeClient
        mWebView.get(mUrl); //请求某个地址
    }

    public FWebViewClient getWebViewClient()
    {
        if (mWebViewClient == null)
        {
            mWebViewClient = new FWebViewClient(this)
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    super.onPageFinished(view, url);
                    /**
                     * 页面加载完成后可以把webview的cookie同步到http框架
                     */
                    FWebViewManager.getInstance().synchronizeWebViewCookieToHttp(url);
                }
            };
        }
        return mWebViewClient;
    }

    public FWebChromeClient getWebChromeClient()
    {
        if (mWebChromeClient == null)
        {
            mWebChromeClient = new FWebChromeClient(this)
            {
                @Override
                public void onReceivedTitle(WebView view, String title)
                {
                    super.onReceivedTitle(view, title);
                    mTvTitle.setText(title); //设置标题
                }
            };
        }
        return mWebChromeClient;
    }

    private FWebViewHandler mWebViewHandler = new FWebViewHandler()
    {
        @Override
        public void onInitWebView(WebView webView)
        {
            /**
             * 每个FWebView被创建的时候都会回调此方法，可以做一些通用的初始化
             */
            Log.i(TAG, "onInitWebView:" + webView);
        }

        @Override
        public List<HttpCookie> getHttpCookieForUrl(String url)
        {
            /**
             * 当FWebView加载某个url的时候会回调此方法，可以返回http框架保存的cookie给webview
             */
            Log.i(TAG, "getHttpCookieForUrl:" + url);
            return null;
        }

        @Override
        public void synchronizeWebViewCookieToHttp(String cookie, List<HttpCookie> listCookie, String url)
        {
            /**
             * 当FWebViewManager的synchronizeWebViewCookieToHttp(url)方法被触发的时候会回调此方法，
             * 可以把webview的coookie存到http框架
             */
            Log.i(TAG, "synchronizeWebViewCookieToHttp:" + cookie);
        }
    };
}
