package com.sd.webview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        _binding.btnDemo.setOnClickListener {
            startActivity(Intent(this, DemoActivity::class.java))
        }
        _binding.btnUpload.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }
}