package com.fanwe.lib.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fanwe.lib.webview.client.FWebChromeClient;
import com.fanwe.lib.webview.client.FWebViewClient;
import com.fanwe.lib.webview.cookie.FWebViewCookie;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class FWebView extends WebView
{
    public FWebView(Context context)
    {
        super(context);
        init();
    }

    public FWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private static final String WEBVIEW_CACHE_DIR = "/webviewcache"; // web缓存目录
    private File mCacheDir;

    protected void init()
    {
        FWebViewCookie.init(getContext());

        initSettings(getSettings());

        setWebViewClient(new FWebViewClient(getContext()));
        setWebChromeClient(new FWebChromeClient(getContext()));

        setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
            {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });

        FWebViewManager.getInstance().notifyWebViewInit(this);
        requestFocus();
    }

    protected void initSettings(WebSettings settings)
    {
        setScaleToShowAll(true);
        setSupportZoom(true);
        setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSavePassword(false);

        //Geolocation
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(getCacheDir().getAbsolutePath());

        //Database
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(getCacheDir().getAbsolutePath());

        //AppCache
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCachePath(getCacheDir().getAbsolutePath());
    }

    /**
     * 设置是否把网页按比例缩放到刚好全部展示，默认true
     *
     * @param isScaleToShowAll
     */
    public final void setScaleToShowAll(boolean isScaleToShowAll)
    {
        getSettings().setUseWideViewPort(isScaleToShowAll);
        getSettings().setLoadWithOverviewMode(isScaleToShowAll);
    }

    /**
     * 设置是否支持缩放，默认true
     *
     * @param isSupportZoom
     */
    public final void setSupportZoom(boolean isSupportZoom)
    {
        getSettings().setSupportZoom(isSupportZoom);
        getSettings().setBuiltInZoomControls(isSupportZoom);
    }

    /**
     * 设置是否显示缩放控件，默认false
     *
     * @param display
     */
    public final void setDisplayZoomControls(boolean display)
    {
        getSettings().setDisplayZoomControls(display);
    }

    /**
     * 加载html内容
     *
     * @param htmlContent
     */
    public void loadHtml(String htmlContent)
    {
        if (htmlContent != null)
        {
            loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
        }
    }

    // get
    public void get(String url)
    {
        get(url, null);
    }

    public void get(String url, Map<String, String> params)
    {
        get(url, params, null);
    }

    public void get(String url, Map<String, String> params, Map<String, String> headers)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }

        FWebViewManager.getInstance().synchronizeHttpCookieToWebView(url);

        url = buildGetUrl(url, params);
        if (headers != null && !headers.isEmpty())
        {
            loadUrl(url, headers);
        } else
        {
            loadUrl(url);
        }
    }

    // post
    public void post(String url)
    {
        post(url, null);
    }

    public void post(String url, Map<String, String> params)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }

        FWebViewManager.getInstance().synchronizeHttpCookieToWebView(url);

        byte[] postData = null;
        String postString = buildPostString(params);
        if (!TextUtils.isEmpty(postString))
        {
            postData = Base64.encode(postString.getBytes(), Base64.DEFAULT);
        }
        postUrl(url, postData);
    }

    /**
     * 调用js函数
     *
     * @param function js函数名称
     * @param params   参数
     */
    public void loadJsFunction(String function, Object... params)
    {
        loadJsFunction(buildJsFunctionString(function, params));
    }

    /**
     * 调用js函数
     *
     * @param js
     */
    public void loadJsFunction(String js)
    {
        if (TextUtils.isEmpty(js))
        {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19)
        {
            evaluateJavascript(js, new ValueCallback<String>()
            {
                @Override
                public void onReceiveValue(String arg0)
                {
                }
            });
        } else
        {
            loadUrl("javascript:" + js);
        }
    }

    /**
     * 返回webview缓存目录
     *
     * @return
     */
    public File getCacheDir()
    {
        if (mCacheDir == null)
        {
            mCacheDir = new File(getContext().getCacheDir(), WEBVIEW_CACHE_DIR);
            if (!mCacheDir.exists())
            {
                mCacheDir.mkdirs();
            }
        }
        return mCacheDir;
    }

    //---------- utils start ----------

    private static String buildGetUrl(String baseUrl, Map<String, String> params)
    {
        if (baseUrl == null || baseUrl.isEmpty() ||
                params == null || params.isEmpty())
        {
            return baseUrl;
        }

        StringBuilder sb = new StringBuilder(baseUrl);
        if (!baseUrl.contains("?"))
        {
            sb.append("?");
        } else
        {
            if (!baseUrl.endsWith("&"))
            {
                sb.append("&");
            }
        }

        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> item = it.next();
            sb.append(item.getKey()).append("=").append(item.getValue());
            sb.append('&');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static String buildPostString(Map<String, String> params)
    {
        if (params == null || params.isEmpty())
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> item = it.next();
            sb.append(item.getKey()).append("=").append(item.getValue());
            sb.append('&');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static String buildJsFunctionString(String function, Object... params)
    {
        if (TextUtils.isEmpty(function))
        {
            return "";
        }

        StringBuilder sb = new StringBuilder(function);
        sb.append("(");
        if (params != null && params.length > 0)
        {
            for (Object item : params)
            {
                if (item instanceof String)
                {
                    sb.append("'").append(String.valueOf(item)).append("'");
                } else
                {
                    sb.append(String.valueOf(item));
                }
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    //---------- utils end ----------
}
