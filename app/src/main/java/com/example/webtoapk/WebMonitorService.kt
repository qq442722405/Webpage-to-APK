package com.example.webtoapk



import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder



class WebMonitorService:Service(){



    override fun onCreate(){


        super.onCreate()



        val channel=
            NotificationChannel(

                "web",

                "中控面板后台",

                NotificationManager.IMPORTANCE_LOW

            )



        val manager=
            getSystemService(
                NotificationManager::class.java
            )



        manager.createNotificationChannel(
            channel
        )





        val notification=
            Notification.Builder(
                this,
                "web"
            )

                .setContentTitle(
                    "中控面板运行中"
                )

                .setContentText(
                    "网页后台监控"
                )

                .setSmallIcon(
                    R.drawable.favicon
                )

                .build()




        startForeground(

            1,

            notification

        )



    }





    override fun onBind(
        intent:Intent?
    ):IBinder?{


        return null


    }



}