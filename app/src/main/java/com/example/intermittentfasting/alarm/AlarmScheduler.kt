package com.example.intermittentfasting.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.intermittentfasting.model.FastAlarmItem
import java.time.ZoneId
import javax.inject.Inject

interface AlarmScheduler {
    fun schedule(item: FastAlarmItem)
    fun cancel(itemHash: Int)
}

class AlarmSchedulerImpl @Inject constructor(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: FastAlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmNotificationConstants.ALARM_MESSAGE_KEY, item.message)
            putExtra(AlarmNotificationConstants.ALARM_TITLE_KEY, item.title)
            putExtra(AlarmNotificationConstants.ALARM_TARGET_HOURS_KEY, item.targetHours)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(itemHash: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                itemHash,
                Intent(context,AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}