package com.example.intermittentfasting

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.intermittentfasting.alarm.AlarmNotificationConstants.Companion.CHANNEL_ID
import com.example.intermittentfasting.alarm.AlarmNotificationConstants.Companion.CHANNEL_TITLE
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_TITLE,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                "Notifications that remind you about your current fast"
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
