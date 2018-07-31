package com.fanwe.lib.webview.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanwe.lib.webview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjun on 2018/2/7.
 */
public class FWebViewClient extends WebViewClient
{
    private final Context mContext;

    private final List<String> mListActionViewUrl = new ArrayList<>();
    private final List<String> mListBrowsableUrl = new ArrayList<>();

    public FWebViewClient(Context context)
    {
        if (!(context instanceof Activity))
            throw new IllegalArgumentException("context must be instance of " + Activity.class);

        mContext = context;
        initActionViewUrl();
        initBrowsableUrl();
    }

    //---------- Override start ----------

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if (interceptActionViewUrl(url) || interceptBrowsableUrl(url))
            return true;

        view.loadUrl(url);
        return true;
    }

    //---------- Override end ----------

    private void initActionViewUrl()
    {
        final String[] arrActionViewUrl = getContext().getResources().getStringArray(R.array.lib_webview_arr_action_view_url);
        if (arrActionViewUrl != null)
        {
            for (String item : arrActionViewUrl)
            {
                addActionViewUrl(item);
            }
        }
    }

    private void initBrowsableUrl()
    {
        final String[] arrBrowsableUrl = getContext().getResources().getStringArray(R.array.lib_webview_arr_browsable_url);
        if (arrBrowsableUrl != null)
        {
            for (String item : arrBrowsableUrl)
            {
                addBrowsableUrl(item);
            }
        }
    }

    public final Context getContext()
    {
        return mContext;
    }

    public final void addActionViewUrl(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        if (mListActionViewUrl.contains(url))
            return;

        mListActionViewUrl.add(url);
    }

    public final void addBrowsableUrl(String url)
    {
        if (TextUtils.isEmpty(url))
            return;

        if (mListBrowsableUrl.contains(url))
            return;

        mListBrowsableUrl.add(url);
    }

    private boolean interceptActionViewUrl(String url)
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

    private boolean interceptBrowsableUrl(String url)
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

    private void startActionView(String url)
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

    private void startBrowsable(String url)
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
}
