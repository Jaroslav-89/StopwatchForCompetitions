package com.example.stopwatchforcompetitions.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

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
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return format.format(date)
    }

    fun convertLongToTime(time: Long): String {
        val h = TimeUnit.MILLISECONDS.toHours(time)
        val m = TimeUnit.MILLISECONDS.toMinutes(time)
        val s = TimeUnit.MILLISECONDS.toSeconds(time)
        val mSec = (TimeUnit.MILLISECONDS.toMillis(time) % 10)
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

    fun convertToSpeed(distance: Int, time: Long): String {
        val timeSeconds = TimeUnit.MILLISECONDS.toSeconds(time).toDouble()
        var speed = 0.0
        return if (distance != 0) {
            speed = (((distance.toDouble() / timeSeconds) * 3.6) % 10)
            String.format("%.1f", speed)
        } else {
            "N/A"
        }
    }

    fun convertToPace(distance: Int, time: Long): String {
        val timeSeconds = TimeUnit.MILLISECONDS.toSeconds(time).toDouble()
        var pace = 0.0
        return if (distance != 0) {
            pace = (((timeSeconds / distance.toDouble()) * 1000) / 60)
            String.format("%.2f", pace)
        } else {
            "N/A"
        }
    }
}