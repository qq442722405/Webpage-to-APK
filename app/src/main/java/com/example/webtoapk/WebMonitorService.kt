package com.example.webtoapk

import android.app.*
import android.content.Intent
import android.os.IBinder

class WebMonitorService: Service(){
    override fun onCreate(){
        super.onCreate()
        val ch=NotificationChannel("web","网页声音监控",NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        startForeground(1,Notification.Builder(this,"web")
            .setContentTitle("网页APK运行中")
            .setContentText("后台监控网页声音")
            .setSmallIcon(android.R.drawable.ic_media_play).build())
    }
    override fun onBind(i:Intent?):IBinder?=null
}
