package com.jaroapps.stopwatchforcompetitions.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Util {
    fun getTimeFormat(time: Long): String {
        val h = (time / 3600000)
        val m = (time - h * 3600000) / 60000
        val s = ((time - h * 3600000 - m * 60000) / 1000)
        val mSec = ((time - h * 3600000 - m * 60000) / 100) % 10

        val hh = if (h < 10) {
            if (h < 1) {
                ""
            } else {
                "0$h:"
            }
        } else {
            "$h:"
        }
        val mm = if (m < 10) {
            if (m < 1) {
                "00:"
            } else {
                "0$m:"
            }
        } else {
            "$m:"
        }
        val ss = if (s < 10) {
            if (s < 1) {
                "00."
            } else {
                "0$s."
            }
        } else {
            "$s."
        }
        return ("$hh$mm$ss$mSec")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    fun convertToSpeed(distance: Int, time: Long): String {
        return if (distance != 0) {
            val timeHr = time.toDouble() / 3600000
            val distanceKm = distance.toDouble() / 1000
            val speed = distanceKm / timeHr
            String.format("%.2f", speed)
        } else {
            "N/A"
        }
    }

    fun convertToPace(distance: Int, time: Long): String {
        return if (distance != 0) {
            val timeSec = time.toDouble() / 1000
            val distKm = distance.toDouble() / 1000
            val paceSecKm = timeSec / distKm
            val min = (paceSecKm / 60).toInt()
            val sec = paceSecKm - (min * 60).toDouble()
            return "$min.${sec.toInt()}"
        } else {
            "N/A"
        }
    }
}