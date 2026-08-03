package com.example.webtoapk

import android.app.Activity
import android.os.Bundle
import android.webkit.*
import android.widget.*
import android.content.Context

class MainActivity: Activity() {
    private lateinit var web: WebView
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)

        val sp=getSharedPreferences("cfg",0)
        val url=sp.getString("url","")

        if(url==""){
            val box=LinearLayout(this)
            box.orientation=LinearLayout.VERTICAL
            val edit=EditText(this)
            edit.hint="输入网址 https://"
            val btn=Button(this)
            btn.text="确定"
            box.addView(edit)
            box.addView(btn)
            setContentView(box)
            btn.setOnClickListener{
                sp.edit().putString("url",edit.text.toString()).apply()
                open(edit.text.toString())
            }
        }else{
            open(url)
        }
        startService(android.content.Intent(this,WebMonitorService::class.java))
    }

    private fun open(u:String){
        web=WebView(this)
        web.settings.javaScriptEnabled=true
        web.settings.mediaPlaybackRequiresUserGesture=false
        web.webViewClient=WebViewClient()
        setContentView(web)
        web.loadUrl(u)
    }

    override fun onBackPressed(){
        if(::web.isInitialized && web.canGoBack()) web.goBack()
        else super.onBackPressed()
    }
}
