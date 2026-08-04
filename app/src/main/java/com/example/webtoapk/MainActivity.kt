package com.example.webtoapk

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*

class MainActivity : Activity() {

    private lateinit var web: WebView

    private val HOME_URL = "http://183.167.219.74:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        openWeb()

        try {
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(Intent(this, WebMonitorService::class.java))
            } else {
                startService(Intent(this, WebMonitorService::class.java))
            }
        } catch (e: Exception) {
        }
    }

    private fun openWeb() {
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
        web.loadUrl(HOME_URL)
    }

    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) {
            web.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
