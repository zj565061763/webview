package com.sd.lib.webview.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class FWebChromeClient extends WebChromeClient
{
    public static final int REQUEST_GET_CONTENT = 100;

    private final Context mContext;
    private ProgressBar mProgressBar;
    private ValueCallback<Uri> mContentValueCallback;

    public FWebChromeClient(Context context)
    {
        if (!(context instanceof Activity))
            throw new IllegalArgumentException("context must be instance of " + Activity.class);

        mContext = context;
    }

    //---------- Override start ----------

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        super.onProgressChanged(view, newProgress);
        changeProgressBarIfNeed(newProgress);
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture)
    {
        final Context context = getContext();
        if (context instanceof Activity)
        {
            Activity activity = (Activity) context;
            mContentValueCallback = uploadFile;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            activity.startActivityForResult(intent, REQUEST_GET_CONTENT);
        }
    }

    //---------- Override end ----------

    public final Context getContext()
    {
        return mContext;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode)
        {
            case REQUEST_GET_CONTENT:
                if (data != null)
                {
                    final Uri value = data.getData();
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

    public void setProgressBar(ProgressBar progressBar)
    {
        mProgressBar = progressBar;
        if (progressBar != null)
            progressBar.setMax(100);
    }

    private void changeProgressBarIfNeed(int progress)
    {
        if (mProgressBar == null)
            return;

        mProgressBar.setProgress(progress);
        if (progress == 100)
        {
            mProgressBar.setVisibility(View.GONE);
        } else
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
