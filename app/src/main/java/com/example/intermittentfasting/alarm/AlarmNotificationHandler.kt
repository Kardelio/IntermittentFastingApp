package com.example.intermittentfasting.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.intermittentfasting.MainActivity
import com.example.intermittentfasting.R
import javax.inject.Inject

class AlarmNotificationHandler @Inject constructor(
    private val context: Context
) {
    fun sendNotification(title: String = "", description: String = "") {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, AlarmNotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.no_food)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}
