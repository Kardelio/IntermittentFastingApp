package com.example.intermittentfasting.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHandler: AlarmNotificationHandler

    override fun onReceive(p0: Context, p1: Intent) {
        val title = p1.getStringExtra(AlarmNotificationConstants.ALARM_TITLE_KEY) ?: return
        val message = p1.getStringExtra(AlarmNotificationConstants.ALARM_MESSAGE_KEY) ?: return
        val target = p1.getIntExtra(AlarmNotificationConstants.ALARM_TARGET_HOURS_KEY, 0)
        notificationHandler.sendNotification(title, message)
        Log.d("BK", "Alarm Hit: ${title} ${message}")
    }
}
