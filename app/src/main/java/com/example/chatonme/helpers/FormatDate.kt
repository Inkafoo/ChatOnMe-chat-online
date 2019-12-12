package com.example.chatonme.helpers

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {

    private val onlyTime = SimpleDateFormat("h:mm a", Locale.US)
    private val onlyDate = SimpleDateFormat("d MMM", Locale.US)

    fun getFormattedTime(timeInMillis: Long): String {
        val date = Date(timeInMillis * 1000L)

        return when {
            isToday(date) -> onlyTime.format(date)
            isYesterday(date) -> "Yesterday"
            else -> onlyDate.format(date)
        }

    }


    private fun isYesterday(date: Date): Boolean {
        return DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS)
    }

    private fun isToday(date: Date): Boolean {
        return DateUtils.isToday(date.time)
    }

}