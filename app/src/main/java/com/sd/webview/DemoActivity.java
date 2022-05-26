package com.sd.webview;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.webview.FWebView;
import com.sd.lib.webview.FWebViewManager;
import com.sd.lib.webview.client.FWebChromeClient;
import com.sd.lib.webview.client.FWebViewClient;

public class DemoActivity extends AppCompatActivity {
    public static final String TAG = DemoActivity.class.getSimpleName();
    public static final String URL = "http://www.baidu.com";

    private FWebView mWebView;
    private TextView mTvTitle;
    private ProgressBar mProgressBar;

    private FWebViewClient mWebViewClient;
    private FWebChromeClient mWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mWebView = findViewById(R.id.webview);
        mTvTitle = findViewById(R.id.tv_title);
        mProgressBar = findViewById(R.id.pb_progress);

        mWebView.setWebViewClient(getWebViewClient());     //设置WebViewClient
        mWebView.setWebChromeClient(getWebChromeClient()); //设置WebChromeClient
        mWebView.get(URL); //请求某个地址
    }

    public FWebViewClient getWebViewClient() {
        if (mWebViewClient == null) {
            mWebViewClient = new FWebViewClient(this) {
                @Override
                public void onPageFinished(WebView view, String url) {
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

    public FWebChromeClient getWebChromeClient() {
        if (mWebChromeClient == null) {
            mWebChromeClient = new FWebChromeClient(this) {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    mTvTitle.setText(title); //设置标题
                }
            };
            mWebChromeClient.setProgressBar(mProgressBar); //设置ProgressBar进度条
        }
        return mWebChromeClient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getWebChromeClient().onActivityResult(requestCode, resultCode, data);
    }
}
