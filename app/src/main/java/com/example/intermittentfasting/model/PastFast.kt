package com.example.intermittentfasting.model

import com.example.intermittentfasting.utils.TimeUtils
import java.util.Locale

class TimeContainer(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)

class PastFast(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val length: TimeContainer
) {
    companion object {
        fun toPastFast(locale: Locale, fast: Fast): PastFast {
            return PastFast(
                fast.id,
                TimeUtils.printNiceTime(locale, fast.startTimeUTC),
                TimeUtils.printNiceTime(locale, fast.endTimeUTC),
                TimeUtils.getTimeLengthStringBetweenTwoDates(
                    locale,
                    fast.startTimeUTC,
                    fast.endTimeUTC
                )
            )
        }
    }
}