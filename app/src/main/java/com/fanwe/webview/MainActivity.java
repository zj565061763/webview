package com.fanwe.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.lib.webview.FWebView;

public class MainActivity extends AppCompatActivity
{
    private FWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.webview);
        mWebView.get("http://www.baidu.com");
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
