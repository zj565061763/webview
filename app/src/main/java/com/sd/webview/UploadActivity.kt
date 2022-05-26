package com.sd.webview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.webview.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityUploadBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

        val html = """
            <html>
            <body>
                <form>
                    <input type="file" name="pic" accept="image/gif" />
                </form>
            </body>
            </html>
        """.trimIndent()

        _binding.webview.loadHtml(html)
    }
}