package com.sd.lib.webview.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sd.lib.webview.R;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FWebViewClient extends WebViewClient
{
    private final Context mContext;

    private final List<String> mListUrlActionView = new CopyOnWriteArrayList<>();
    private final List<String> mListUrlBrowsable = new CopyOnWriteArrayList<>();

    public FWebViewClient(Context context)
    {
        if (!(context instanceof Activity))
            throw new IllegalArgumentException("context must be instance of " + Activity.class);

        mContext = context;
        initUrlActionView();
        initUrlBrowsable();
    }

    public final Context getContext()
    {
        return mContext;
    }

    //---------- Override start ----------

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if (url.startsWith("http:") || url.startsWith("https:"))
        {
            view.loadUrl(url);
            return true;
        }

        if (interceptUrlActionView(url) || interceptUrlBrowsable(url))
            return true;

        return false;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
        handler.proceed();
    }

    //---------- Override end ----------

    private void initUrlActionView()
    {
        final String[] array = getContext().getResources().getStringArray(R.array.lib_webview_arr_action_view_url);
        if (array != null)
        {
            for (String item : array)
            {
                addUrlActionView(item);
            }
        }
    }

    private void initUrlBrowsable()
    {
        final String[] array = getContext().getResources().getStringArray(R.array.lib_webview_arr_browsable_url);
        if (array != null)
        {
            for (String item : array)
            {
                addUrlBrowsable(item);
            }
        }
    }

    public final void addUrlActionView(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        if (mListUrlActionView.contains(url))
            return;

        mListUrlActionView.add(url);
    }

    public final void addUrlBrowsable(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        if (mListUrlBrowsable.contains(url))
            return;

        mListUrlBrowsable.add(url);
    }

    public final void clearUrlActionView()
    {
        mListUrlActionView.clear();
    }

    public final void clearUrlBrowsable()
    {
        mListUrlBrowsable.clear();
    }

    protected boolean interceptUrlActionView(String url)
    {
        for (String item : mListUrlActionView)
        {
            if (url.startsWith(item))
            {
                startActionView(url);
                return true;
            }
        }
        return false;
    }

    protected boolean interceptUrlBrowsable(String url)
    {
        for (String item : mListUrlBrowsable)
        {
            if (url.startsWith(item))
            {
                startBrowsable(url);
                return true;
            }
        }
        return false;
    }

    private void startActionView(String url)
    {
        try
        {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            getContext().startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startBrowsable(String url)
    {
        try
        {
            final Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            getContext().startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
