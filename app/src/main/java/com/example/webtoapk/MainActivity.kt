package com.example.webtoapk

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import android.widget.*
import android.content.Intent


class MainActivity : Activity() {


    private lateinit var web: WebView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        // 全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val sp = getSharedPreferences("cfg",0)


        val url = sp.getString("url","") ?: ""


        if(url.isEmpty()){


            showInputPage(sp)



        }else{


            openWeb(url)


        }



        // 启动后台服务
        startService(
            Intent(
                this,
                WebMonitorService::class.java
            )
        )

    }



    private fun showInputPage(sp: android.content.SharedPreferences){


        val layout = LinearLayout(this)

        layout.orientation = LinearLayout.VERTICAL


        val edit = EditText(this)

        edit.hint = "请输入网址 https://"


        val button = Button(this)

        button.text = "确定"



        layout.addView(edit)

        layout.addView(button)



        setContentView(layout)



        button.setOnClickListener {


            var input = edit.text.toString()


            if(input.isNotEmpty()){


                if(!input.startsWith("http")){

                    input = "https://$input"

                }


                sp.edit()
                    .putString("url",input)
                    .apply()



                openWeb(input)


            }


        }


    }



    private fun openWeb(url:String){


        web = WebView(this)


        val settings = web.settings


        settings.javaScriptEnabled = true


        settings.domStorageEnabled = true


        settings.mediaPlaybackRequiresUserGesture = false


        settings.loadWithOverviewMode = true


        settings.useWideViewPort = true



        web.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {


                view?.loadUrl(
                    request?.url.toString()
                )


                return true

            }

        }



        setContentView(web)



        web.loadUrl(url)



    }



    override fun onBackPressed(){


        if(::web.isInitialized && web.canGoBack()){


            web.goBack()


        }else{


            super.onBackPressed()

        }


    }


}