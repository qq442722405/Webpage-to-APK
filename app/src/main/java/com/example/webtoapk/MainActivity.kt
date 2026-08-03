package com.example.webtoapk


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import android.widget.*
import androidx.core.app.ActivityCompat



class MainActivity : Activity(){



    private lateinit var web: WebView



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)



        // 全屏
        window.setFlags(

            WindowManager.LayoutParams.FLAG_FULLSCREEN,

            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )



        // Android 13通知权限

        if(Build.VERSION.SDK_INT >= 33){


            if(checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED){


                ActivityCompat.requestPermissions(

                    this,

                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),

                    100

                )

            }


        }





        val sp=getSharedPreferences(
            "cfg",
            MODE_PRIVATE
        )



        val url =
            sp.getString("url","") ?: ""




        if(url.isEmpty()){


            showInput(sp)



        }else{


            openWeb(url)



        }




        // 启动后台通知服务

        try {


            startForegroundService(

                Intent(
                    this,
                    WebMonitorService::class.java
                )

            )


        }catch(e:Exception){



            startService(

                Intent(
                    this,
                    WebMonitorService::class.java
                )

            )


        }



    }






    private fun showInput(
        sp: android.content.SharedPreferences
    ){



        val layout=LinearLayout(this)


        layout.orientation =
            LinearLayout.VERTICAL




        val edit=EditText(this)


        edit.hint="请输入网址 https://"



        val button=Button(this)


        button.text="确定"




        layout.addView(edit)


        layout.addView(button)



        setContentView(layout)






        button.setOnClickListener {



            var url=
                edit.text.toString().trim()



            if(url.isNotEmpty()){



                if(!url.startsWith("http")){


                    url="https://$url"


                }




                sp.edit()

                    .putString(
                        "url",
                        url
                    )

                    .apply()



                openWeb(url)



            }



        }



    }








    private fun openWeb(
        url:String
    ){



        web=WebView(this)



        val s=web.settings



        s.javaScriptEnabled=true


        s.domStorageEnabled=true


        s.databaseEnabled=true


        s.allowFileAccess=true


        s.mediaPlaybackRequiresUserGesture=false


        s.loadWithOverviewMode=true


        s.useWideViewPort=true




        web.webViewClient=
            object:WebViewClient(){



                override fun shouldOverrideUrlLoading(

                    view:WebView?,

                    request:WebResourceRequest?

                ):Boolean{


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


        if(::web.isInitialized &&
            web.canGoBack()){


            web.goBack()


        }else{


            super.onBackPressed()


        }



    }



}