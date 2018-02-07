package com.fanwe.lib.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zhengjun on 2016/7/12.
 */
public class WebViewClientWrapper extends WebViewClient
{
    private WebViewClient mWebViewClient;

    public void setWebViewClient(WebViewClient webViewClient)
    {
        this.mWebViewClient = webViewClient;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if (mWebViewClient != null)
        {
            return mWebViewClient.shouldOverrideUrlLoading(view, url);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        super.onPageStarted(view, url, favicon);
        if (mWebViewClient != null)
        {
            mWebViewClient.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        super.onPageFinished(view, url);
        if (mWebViewClient != null)
        {
            mWebViewClient.onPageFinished(view, url);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url)
    {
        super.onLoadResource(view, url);
        if (mWebViewClient != null)
        {
            mWebViewClient.onLoadResource(view, url);
        }
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url)
    {
        if (mWebViewClient != null)
        {
            return mWebViewClient.shouldInterceptRequest(view, url);
        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            if (mWebViewClient != null)
            {
                return mWebViewClient.shouldInterceptRequest(view, request);
            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg)
    {
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
        if (mWebViewClient != null)
        {
            mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (mWebViewClient != null)
        {
            mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend)
    {
        super.onFormResubmission(view, dontResend, resend);
        if (mWebViewClient != null)
        {
            mWebViewClient.onFormResubmission(view, dontResend, resend);
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
    {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (mWebViewClient != null)
        {
            mWebViewClient.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
        super.onReceivedSslError(view, handler, error);
        if (mWebViewClient != null)
        {
            mWebViewClient.onReceivedSslError(view, handler, error);
        }
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request)
    {
        super.onReceivedClientCertRequest(view, request);
        if (Build.VERSION.SDK_INT >= 21)
        {
            if (mWebViewClient != null)
            {
                mWebViewClient.onReceivedClientCertRequest(view, request);
            }
        }
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
    {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
        if (mWebViewClient != null)
        {
            mWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
    {
        if (mWebViewClient != null)
        {
            return mWebViewClient.shouldOverrideKeyEvent(view, event);
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event)
    {
        super.onUnhandledKeyEvent(view, event);
        if (mWebViewClient != null)
        {
            mWebViewClient.onUnhandledKeyEvent(view, event);
        }
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale)
    {
        super.onScaleChanged(view, oldScale, newScale);
        if (mWebViewClient != null)
        {
            mWebViewClient.onScaleChanged(view, oldScale, newScale);
        }
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args)
    {
        super.onReceivedLoginRequest(view, realm, account, args);
        if (mWebViewClient != null)
        {
            mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
        }
    }
}
