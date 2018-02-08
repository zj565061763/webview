package com.fanwe.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.fanwe.lib.webview.FWebView;
import com.fanwe.lib.webview.FWebViewManager;
import com.fanwe.lib.webview.client.FWebViewClient;

public class MainActivity extends AppCompatActivity
{
    private FWebView mWebView;
    private String mUrl = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.webview);

        mWebView.setWebViewClient(new FWebViewClient(this)
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                FWebViewManager.getInstance().synchronizeWebViewCookieToHttp(url);
            }
        });
        mWebView.get(mUrl);
    }

    @Override
    public void onBackPressed()
    {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        } else
        {
            super.onBackPressed();
        }
    }
}
