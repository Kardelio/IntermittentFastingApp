package com.example.utils

//import com.example.intermittentfasting.model.TimeContainer
import com.example.utils.model.TimeContainer
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeUtils {
    companion object {

        enum class TimeType {
            SECONDS,
            MINUTE,
            HOUR
        }

        private fun getDateFromString(locale: Locale, str: String): Date {
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            return simpleDateFormat.parse(str) as Date
        }

        fun plusXHoursToTime(locale: Locale, timeIn: String, plusAmount: Int): Long {
            val timeInStr = getDateFromString(locale, timeIn)
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            calendar.time = timeInStr
            calendar.add(Calendar.HOUR, plusAmount)
            return calendar.timeInMillis
        }

        fun getCurrentTimePlusXHours(plusAmount: Int): Long {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            calendar.add(Calendar.HOUR, plusAmount)
            return calendar.timeInMillis
        }

        fun getCurrentUTCTimeString(locale: Locale): String {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            return simpleDateFormat.format(calendar.time)
        }

        fun getHourOfTheDay(): Int {
            val calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.HOUR_OF_DAY)
        }

        fun getLengthOfFast(start: Long, end: Long): Long {
            return end - start
        }

        fun calculateFinishTimeWithTarget(
            locale: Locale,
            targetHours: Int,
            startTimeStr: String
        ): String {
            println("End time target: ${targetHours} -> ${startTimeStr}")
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val a = getDateFromString(locale, startTimeStr)
            calendar.time = a
            calendar.add(Calendar.HOUR, targetHours)
            val sdf = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            val str = sdf.format(calendar.time)


            println("--- time target: ${targetHours} -> ${str}")


            return str
        }

        fun getDayDifference(
            locale: Locale,
            currentEndTimeStr: String,
            previousStartTimeStr: String
        ): Long {
            val currentEnd = getDateFromString(locale, currentEndTimeStr)
            val previousStart = getDateFromString(locale, previousStartTimeStr)
//            val diff = Math.abs(previousStart.time - currentEnd.time)
//           val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            val t1 = currentEnd.toInstant().truncatedTo(ChronoUnit.DAYS)
            val t2 = previousStart.toInstant().truncatedTo(ChronoUnit.DAYS)
            return ChronoUnit.DAYS.between(t1, t2)
//           return days.toInt()
//            var different = previousStart.time - currentEnd.time
//
//            val secondsInMilli: Long = 1000
//            val minutesInMilli = secondsInMilli * 60
//            val hoursInMilli = minutesInMilli * 60
//            val daysInMilli = hoursInMilli * 24
//
//            val elapsedDays: Long = different / daysInMilli
//            different %= daysInMilli
//            return elapsedDays.toInt()
        }

        fun getMinuteOfTheDay(): Int {
            val calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.MINUTE)
        }

        fun convertHoursToSeconds(hours: Int): Int {
            return hours * 3600
        }

        fun convertMillisToX(millis: Long, timeType: TimeType = TimeType.SECONDS): Long {
            //TODO handle timeypes
            return millis / 1000
        }

        fun getLengthInTimeFromLongWithVariableDay(length: Long): String {
            var different = length
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
            return if (elapsedDays > 0) {
                "${elapsedDays}d ${elapsedHours}h ${elapsedMinutes}m ${elapsedSeconds}s"
            } else {
                "${elapsedHours}h ${elapsedMinutes}m ${elapsedSeconds}s"
            }
        }

        fun getLengthInTimeFromLong(length: Long): String {
            var different = length
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

        fun getTimeRemainingCountDown(locale: Locale, startTime: String, targetHours: Int): String {
            //hours minutes seconds
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val calendara: Calendar = Calendar.getInstance(timeZone)
            val a = getDateFromString(locale, startTime)
            calendar.time = a
            calendar.add(Calendar.HOUR, targetHours)
            val sdf = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
            val str = sdf.format(calendar.time)
            val early = sdf.format(calendara.time)

            val b = getTimeDifferenceBetweenTwoTimes(locale, early, str)
            return b
        }

        fun getTimeDifferenceBetweenTwoTimes(
            locale: Locale,
            earlierTime: String,
            laterTime: String
        ): String {
            val start = getDateFromString(locale, earlierTime)
            val end = getDateFromString(locale, laterTime)
            var different = end.time - start.time
            return getLengthInTimeFromLongWithVariableDay(different)
        }

        fun getTimeDifferenceToNow(locale: Locale, startTime: String): String {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val start = getDateFromString(locale, startTime)
            var different = calendar.time.time - start.time
            println(different)
            return getLengthInTimeFromLongWithVariableDay(different)
        }

        fun getSecondsDifferenceToNow(locale: Locale, startTime: String): Long {
            val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
            val calendar: Calendar = Calendar.getInstance(timeZone)
            val start = getDateFromString(locale, startTime)
            var different = calendar.time.time - start.time
            return convertMillisToX(different)
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

