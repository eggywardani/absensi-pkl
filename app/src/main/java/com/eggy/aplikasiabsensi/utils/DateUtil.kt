package com.eggy.aplikasiabsensi.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
//    private val formatUTC = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//
//    fun fromTimeStampToDate(): Date?{
//       formatUTC.timeZone = TimeZone.getTimeZone("UTC")
//        return try {
//            formatUTC.parse("2021-01-04T13:53:29.000000Z")
//        }catch (e: ParseException){
//            null
//        }
//    }

    fun String.stringToDate(): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            format.parse(this)
        } catch (e: ParseException) {
            Log.e("e", "${e.message}")
            null
        }


    }



    fun Date.toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }

    fun Date.toDay(): String {
        val outputFormat = SimpleDateFormat("dd", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Date.toMonth(): String {
        val outputFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Date.toTime(): String {
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Calendar.toDate(): Date {
        return this.time
    }

    fun getCurrentDateForServer(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}