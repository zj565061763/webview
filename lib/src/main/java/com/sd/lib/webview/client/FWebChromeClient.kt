package com.sd.lib.webview.client

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

open class FWebChromeClient(context: Context?) : WebChromeClient() {
    private val context: Activity

    private var _progressBar: ProgressBar? = null
    private var _valueCallback: ValueCallback<Array<Uri>>? = null

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
        _valueCallback = filePathCallback

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            this.addCategory(Intent.CATEGORY_OPENABLE)
            this.type = "image/*"
        }
        val chooserIntent = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, intent)
            this.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        }
        context.startActivityForResult(chooserIntent, REQUEST_CODE_GET_CONTENT)
        return true
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_GET_CONTENT -> {
                _valueCallback?.let { callback ->
                    var value: Uri? = null
                    if (resultCode == Activity.RESULT_OK) {
                        value = data?.data
                    }

                    if (value == null) {
                        callback.onReceiveValue(null)
                    } else {
                        callback.onReceiveValue(arrayOf(value))
                    }
                    _valueCallback = null
                }
            }
            else -> {}
        }
    }

    companion object {
        const val REQUEST_CODE_GET_CONTENT = 100
    }

    init {
        require(context is Activity) { "context must be instance of " + Activity::class.java }
        this.context = context
    }
}