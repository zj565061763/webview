package com.fanwe.lib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static final int REQUEST_GET_CONTENT = 100;
    private static final String WEBVIEW_CACHE_DIR = "/webviewcache"; // web缓存目录

    private ValueCallback<Uri> mContentValueCallback;
    private List<String> mListActionViewUrl = new ArrayList<>();
    private List<String> mListBrowsableUrl = new ArrayList<>();
    private File mCacheDir;
    private ProgressBar mProgressBar;

    protected void init()
    {
        FWebViewCookie.init(getContext());

        initActionViewUrl();
        initBrowsableUrl();
        initSettings(getSettings());

        setWebViewClient(mWebViewClientWrapper);
        setWebChromeClient(mWebChromeClientWrapper);

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
        requestFocus();

        FWebViewManager.getInstance().notifyWebViewInit(this);
    }

    private void initActionViewUrl()
    {
        addActionViewUrl("tel:");
        addActionViewUrl("weixin:");
        addActionViewUrl("appay:");
        addActionViewUrl("sinaweibo:");
        addActionViewUrl("alipayqr");
        addActionViewUrl("alipays");
        addActionViewUrl("mqqapi://");
    }

    private void initBrowsableUrl()
    {
        addBrowsableUrl("intent://platformapi/startapp");
        addBrowsableUrl("intent://dl/business");
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
     * 设置进度条ProgressBar
     *
     * @param progressBar
     */
    public void setProgressBar(ProgressBar progressBar)
    {
        mProgressBar = progressBar;
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

    public void setWebViewClientListener(WebViewClient listener)
    {
        mWebViewClientWrapper.setWebViewClient(listener);
    }

    public void setWebChromeClientListener(WebChromeClient listener)
    {
        mWebChromeClientWrapper.setWebChromeClient(listener);
    }

    /**
     * WebViewClient
     */
    private WebViewClientWrapper mWebViewClientWrapper = new WebViewClientWrapper()
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (super.shouldOverrideUrlLoading(view, url))
            {
            } else
            {
                if (interceptActionViewUrl(url) || interceptBrowsableUrl(url))
                {
                    return true;
                }
                view.loadUrl(url);
            }
            return true;
        }
    };

    /**
     * WebChromeClient
     */
    private WebChromeClientWrapper mWebChromeClientWrapper = new WebChromeClientWrapper()
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            super.onProgressChanged(view, newProgress);
            changeProgressBarIfNeed(newProgress);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture)
        {
            Context context = getContext();
            if (context instanceof Activity)
            {
                Activity activity = (Activity) context;
                mContentValueCallback = uploadFile;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                activity.startActivityForResult(intent, REQUEST_GET_CONTENT);
            }
            super.openFileChooser(uploadFile, acceptType, capture);
        }
    };

    public void addActionViewUrl(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        if (!mListActionViewUrl.contains(url))
        {
            mListActionViewUrl.add(url);
        }
    }

    public void addBrowsableUrl(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        if (!mListBrowsableUrl.contains(url))
        {
            mListBrowsableUrl.add(url);
        }
    }

    protected boolean interceptActionViewUrl(String url)
    {
        for (String item : mListActionViewUrl)
        {
            if (url.startsWith(item))
            {
                startActionView(url);
                return true;
            }
        }
        return false;
    }

    protected boolean interceptBrowsableUrl(String url)
    {
        for (String item : mListBrowsableUrl)
        {
            if (url.startsWith(item))
            {
                startBrowsable(url);
                return true;
            }
        }
        return false;
    }

    protected void startActionView(String url)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            getContext().startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void startBrowsable(String url)
    {
        try
        {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            getContext().startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void changeProgressBarIfNeed(int progress)
    {
        if (mProgressBar != null)
        {
            if (progress == 100)
            {
                mProgressBar.setVisibility(View.GONE);
            } else
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_GET_CONTENT:
                    if (data != null)
                    {
                        Uri value = data.getData();
                        if (value != null)
                        {
                            mContentValueCallback.onReceiveValue(value);
                            mContentValueCallback = null;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
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
