package com.sd.webview;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.webview.client.FWebChromeClient;
import com.sd.webview.databinding.ActivityDemoBinding;

public class DemoActivity extends AppCompatActivity {
    private ActivityDemoBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        _binding.webview.setWebChromeClient(_webChromeClient);
        _webChromeClient.setProgressBar(_binding.pbProgress);
        _binding.webview.get("http://www.baidu.com");
    }

    private final FWebChromeClient _webChromeClient = new FWebChromeClient(this) {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置标题
            _binding.tvTitle.setText(title);
        }
    };
}
