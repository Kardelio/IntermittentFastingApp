package com.example.intermittentfasting.utils

import com.example.intermittentfasting.model.TimeContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeUtils {
    companion object {
        private fun getDateFromString(locale: Locale, str: String): Date {
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            return simpleDateFormat.parse(str) as Date
        }

        fun getCurrentUTCTimeString(locale: Locale): String {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            return simpleDateFormat.format(calendar.time)
        }

        fun getTimeDifferenceToNow(locale: Locale, startTime: String): String {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val start = getDateFromString(locale, startTime)
            var different = calendar.time.time - start.time

            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays: Long = different / daysInMilli
            different %= daysInMilli

            val elapsedHours: Long = different / hoursInMilli
            different %= hoursInMilli

            val elapsedMinutes: Long = different / minutesInMilli
            different %= minutesInMilli

            val elapsedSeconds: Long = different / secondsInMilli
            return "${elapsedDays}d ${elapsedHours}h ${elapsedMinutes}m ${elapsedSeconds}s"
        }

        fun isFirstDateBeforeOther(locale: Locale, dateOne: String, dateTwo: String): Boolean {
            val a = getDateFromString(locale, dateOne)
            val b = getDateFromString(locale, dateTwo)
            return b.after(a)
        }

        fun convertStringsToUTCString(
            locale: Locale,
            day: Int,
            month: Int,
            year: Int,
            hour: Int,
            minute: Int
        ): String {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val cleanDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", locale)
            val date1 = cleanDateFormat.parse("${day}-${(month + 1)}-${year} ${hour}:${minute}")
            val cal = Calendar.getInstance(timeZone)
            cal.time = date1
            calendar.set(Calendar.DATE, (day - 1))
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            return simpleDateFormat.format(cal.time)
        }

        fun getTimeLengthStringBetweenTwoDates(
            locale: Locale,
            dateStart: String,
            dateEnd: String
        ): TimeContainer {
            val start = getDateFromString(locale, dateStart)
            val end = getDateFromString(locale, dateEnd)

            var different = end.time - start.time

            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays: Long = different / daysInMilli
            different %= daysInMilli

            val elapsedHours: Long = different / hoursInMilli
            different %= hoursInMilli

            val elapsedMinutes: Long = different / minutesInMilli
            different %= minutesInMilli

            val elapsedSeconds: Long = different / secondsInMilli


//            return "${elapsedDays}d ${elapsedHours}h ${elapsedMinutes}m ${elapsedSeconds}s"
//            return "${elapsedHours}h ${elapsedMinutes}m"
            return TimeContainer(elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
        }

        fun getTimestampFromString(locale: Locale, timeStr: String): Long {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            calendar.time = simpleDateFormat.parse(timeStr) as Date
            return calendar.timeInMillis
        }

        fun printNiceTime(locale: Locale, inTime: String): String {

            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            val cleanDateFormat = SimpleDateFormat("EE MMM dd HH:mm", locale)
            calendar.time = simpleDateFormat.parse(inTime) as Date
            return cleanDateFormat.format(calendar.time)
        }
    }
}

