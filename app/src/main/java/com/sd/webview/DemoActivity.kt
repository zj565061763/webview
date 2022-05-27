package com.sd.webview

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.sd.lib.webview.client.FWebChromeClient
import com.sd.webview.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityDemoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

        _webChromeClient.setProgressBar(_binding.pbProgress)
        _binding.webview.webChromeClient = _webChromeClient
        _binding.webview.get("http://www.baidu.com")
    }

    private val _webChromeClient: FWebChromeClient = object : FWebChromeClient(this) {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            // 设置标题
            _binding.tvTitle.text = title
        }
    }
}