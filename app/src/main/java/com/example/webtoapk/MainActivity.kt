package com.example.webtoapk

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.webkit.*
import android.widget.*

class MainActivity : Activity() {

    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val sp = getSharedPreferences("cfg", MODE_PRIVATE)
        val url = sp.getString("url", "") ?: ""

        if (url.isEmpty()) {
            showInput(sp)
        } else {
            openWeb(url)
        }

        try {
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(
                    Intent(this, WebMonitorService::class.java)
                )
            } else {
                startService(
                    Intent(this, WebMonitorService::class.java)
                )
            }
        } catch (_: Exception) {
        }
    }

    private fun showInput(sp: android.content.SharedPreferences) {

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER

        val edit = EditText(this)
        edit.hint = "请输入网址或IP地址"

        val button = Button(this)
        button.text = "确定"

        layout.addView(edit)
        layout.addView(button)

        setContentView(layout)

        button.setOnClickListener {

            var url = edit.text.toString().trim()

            if (url.isNotEmpty()) {

                if (!url.startsWith("http")) {
                    url = "http://$url"
                }

                sp.edit()
                    .putString("url", url)
                    .apply()

                openWeb(url)
            }
        }
    }

    private fun openWeb(url: String) {

        web = WebView(this)

        val setting = web.settings

        setting.javaScriptEnabled = true
        setting.domStorageEnabled = true
        setting.databaseEnabled = true
        setting.allowFileAccess = true
        setting.mediaPlaybackRequiresUserGesture = false
        setting.loadWithOverviewMode = true
        setting.useWideViewPort = true

        web.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        setContentView(web)

        web.loadUrl(url)
    }

    override fun onBackPressed() {

        if (::web.isInitialized && web.canGoBack()) {
            web.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
