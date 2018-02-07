package com.fanwe.lib.webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * Created by zhengjun on 2016/7/12.
 */
public class WebChromeClientWrapper extends WebChromeClient
{
    private WebChromeClient mWebChromeClient;

    public void setWebChromeClient(WebChromeClient webChromeClient)
    {
        this.mWebChromeClient = webChromeClient;
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture)
    {

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        super.onProgressChanged(view, newProgress);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title)
    {
        super.onReceivedTitle(view, title);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onReceivedTitle(view, title);
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon)
    {
        super.onReceivedIcon(view, icon);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onReceivedIcon(view, icon);
        }
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed)
    {
        super.onReceivedTouchIconUrl(view, url, precomposed);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback)
    {
        super.onShowCustomView(view, callback);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onShowCustomView(view, callback);
        }
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback)
    {
        super.onShowCustomView(view, requestedOrientation, callback);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
        }
    }

    @Override
    public void onHideCustomView()
    {
        super.onHideCustomView();
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onHideCustomView();
        }
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view)
    {
        super.onRequestFocus(view);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onRequestFocus(view);
        }
    }

    @Override
    public void onCloseWindow(WebView window)
    {
        super.onCloseWindow(window);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onCloseWindow(window);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onJsAlert(view, url, message, result);
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onJsConfirm(view, url, message, result);
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onJsBeforeUnload(view, url, message, result);
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater)
    {
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        }
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater)
    {
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
    {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    @Override
    public void onGeolocationPermissionsHidePrompt()
    {
        super.onGeolocationPermissionsHidePrompt();
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onGeolocationPermissionsHidePrompt();
        }
    }

    @Override
    public void onPermissionRequest(PermissionRequest request)
    {
        super.onPermissionRequest(request);
        if (Build.VERSION.SDK_INT >= 21)
        {
            if (mWebChromeClient != null)
            {
                mWebChromeClient.onPermissionRequest(request);
            }
        }
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request)
    {
        super.onPermissionRequestCanceled(request);
        if (Build.VERSION.SDK_INT >= 21)
        {
            if (mWebChromeClient != null)
            {
                mWebChromeClient.onPermissionRequestCanceled(request);
            }
        }
    }

    @Override
    public boolean onJsTimeout()
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onJsTimeout();
        }
        return super.onJsTimeout();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID)
    {
        super.onConsoleMessage(message, lineNumber, sourceID);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage)
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.onConsoleMessage(consoleMessage);
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster()
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.getDefaultVideoPoster();
        }
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView()
    {
        if (mWebChromeClient != null)
        {
            return mWebChromeClient.getVideoLoadingProgressView();
        }
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback)
    {
        super.getVisitedHistory(callback);
        if (mWebChromeClient != null)
        {
            mWebChromeClient.getVisitedHistory(callback);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            if (mWebChromeClient != null)
            {
                return mWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}
