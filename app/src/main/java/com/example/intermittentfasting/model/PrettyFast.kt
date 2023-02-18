package com.example.intermittentfasting.model

import com.example.utils.TimeUtils
import java.util.Locale

class PrettyFast(
    val isActive: Boolean,
    val startTime: String,
    val endTime: String?,
    val explicitStart: String,
    val explicitEnd: String?,
    val targetHours: Int,
    val endTimeToDisplay: String
) {
    companion object {
        fun toPrettyFast(locale: Locale, fast: Fast): PrettyFast {
            return PrettyFast(
                fast.isActive(),
                TimeUtils.printNiceTime(locale, fast.startTimeUTC),
                if (fast.endTimeUTC.isNotBlank())
                    TimeUtils.printNiceTime(
                        locale,
                        fast.endTimeUTC
                    )
                else
                    null,
                fast.startTimeUTC,
                fast.endTimeUTC.ifBlank { null },
                fast.targetHours,
                TimeUtils.printNiceTime(
                    locale,
                    TimeUtils.calculateFinishTimeWithTarget(
                        locale,
                        fast.targetHours,
                        fast.startTimeUTC
                    )
                )
            )
        }
    }
}
