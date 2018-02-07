package com.fanwe.lib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
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

import com.fanwe.lib.utils.context.FDeviceUtil;
import com.fanwe.lib.utils.context.FPackageUtil;
import com.fanwe.lib.utils.context.FResUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FWebView extends WebView
{
    public static final int REQUEST_GET_CONTENT = 100;
    private static final String WEBVIEW_CACHE_DIR = "/webviewcache"; // web缓存目录

    private ValueCallback<Uri> mContentValueCallback;
    private List<String> mListActionViewUrl = new ArrayList<>();
    private List<String> mListBrowsableUrl = new ArrayList<String>();
    private File mCacheDir;
    private ProgressBar mProgressBar;

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

    public void setProgressBar(ProgressBar progressBar)
    {
        this.mProgressBar = progressBar;
    }

    public void addActionViewUrl(String url)
    {
        if (url == null)
        {
            return;
        }
        if (!mListActionViewUrl.contains(url))
        {
            mListActionViewUrl.add(url);
        }
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

    public void addBrowsableUrl(String url)
    {
        if (url == null)
        {
            return;
        }
        if (!mListBrowsableUrl.contains(url))
        {
            mListBrowsableUrl.add(url);
        }
    }

    private void initBrowsableUrl()
    {
        addBrowsableUrl("intent://platformapi/startapp");
        addBrowsableUrl("intent://dl/business");
    }

    public boolean interceptActionViewUrl(String url)
    {
        boolean result = false;
        if (url != null)
        {
            for (String item : mListActionViewUrl)
            {
                if (url.startsWith(item))
                {
                    startActionView(url);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean interceptBrowsableUrl(String url)
    {
        boolean result = false;
        if (url != null)
        {
            for (String item : mListBrowsableUrl)
            {
                if (url.startsWith(item))
                {
                    startBrowsable(url);
                    result = true;
                    break;
                }
            }
        }
        return result;
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
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setComponent(null);
            getContext().startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void init()
    {
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + WEBVIEW_CACHE_DIR;
        mCacheDir = new File(cacheDirPath);
        if (!mCacheDir.exists())
        {
            mCacheDir.mkdirs();
        }

        initActionViewUrl();
        initBrowsableUrl();
        initSettings(getSettings());

        setWebViewClient(mViewClientWrapper);
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
    }

    public void setWebViewClientListener(WebViewClient listener)
    {
        mViewClientWrapper.setWebViewClient(listener);
    }

    public void setWebChromeClientListener(WebChromeClient listener)
    {
        mWebChromeClientWrapper.setWebChromeClient(listener);
    }

    /**
     * WebViewClient
     */
    private WebViewClientWrapper mViewClientWrapper = new WebViewClientWrapper()
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (interceptActionViewUrl(url))
            {
                return true;
            }

            if (interceptBrowsableUrl(url))
            {
                return true;
            }

            view.loadUrl(url);

            super.shouldOverrideUrlLoading(view, url);
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

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                activity.startActivityForResult(intent, REQUEST_GET_CONTENT);
            }
            super.openFileChooser(uploadFile, acceptType, capture);
        }
    };

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

    protected void initSettings(WebSettings settings)
    {
        setScaleToShowAll(true);
        setSupportZoom(true);
        setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true); // 开启DOM storage API 功能
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(false);

        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(mCacheDir.getAbsolutePath());

        // Database
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(mCacheDir.getAbsolutePath());

        // AppCache
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCachePath(mCacheDir.getAbsolutePath());

        PackageInfo packageInfo = FPackageUtil.getPackageInfo();

        String us = settings.getUserAgentString();
        us = us + " fanwe_app_sdk" +
                " sdk_type/android" +
                " sdk_version_name/" + packageInfo.versionName +
                " sdk_version/" + packageInfo.versionCode +
                " sdk_guid/" + FDeviceUtil.getDeviceId() +
                " screen_width/" + FResUtil.getScreenWidth() +
                " screen_height/" + FResUtil.getScreenHeight();
        settings.setUserAgentString(us);
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
