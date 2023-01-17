package com.example.intermittentfasting.model

import com.example.utils.TimeUtils
import com.example.utils.model.TimeContainer
import java.util.Locale

//class TimeContainer(
//    val days: Long,
//    val hours: Long,
//    val minutes: Long,
//    val seconds: Long
//)

open class FastContainer

class EmptyFast(
    val daysMissed: Int = 0
) : FastContainer()

class PastFast(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val length: TimeContainer,
    val targetHours: Int
) : FastContainer() {
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
                ),
                fast.targetHours
            )
        }
    }
}