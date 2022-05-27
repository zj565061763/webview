package com.sd.lib.webview.client

import android.content.Context
import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import com.sd.lib.actresult.FActivityResult

open class FWebChromeClient(context: Context) : WebChromeClient() {
    private val _activity: ComponentActivity
    private var _progressBar: ProgressBar? = null
    private var _activityResult: FActivityResult? = null

    fun setProgressBar(progressBar: ProgressBar?) {
        _progressBar = progressBar
        if (progressBar != null) {
            progressBar.max = 100
        }
    }

    //---------- Override start ----------
    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        _progressBar?.let { progressBar ->
            progressBar.progress = newProgress
            if (newProgress == 100) {
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
    ): Boolean {
        if (_activityResult == null) {
            _activityResult = FActivityResult(_activity)
        }
        _activityResult?.registerResult { result ->
            val value = FileChooserParams.parseResult(result.resultCode, result.data)
            filePathCallback.onReceiveValue(value)
        }?.launch(fileChooserParams.createIntent())
        return true
    }

    init {
        require(context is ComponentActivity) { "activity must be instance of ${ComponentActivity::class.java}" }
        this._activity = context
    }
}