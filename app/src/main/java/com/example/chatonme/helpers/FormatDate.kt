package com.example.chatonme.helpers

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {

    private val onlyTime = SimpleDateFormat("h:mm a", Locale.US)
    private val onlyDate = SimpleDateFormat("d MMM", Locale.US)

    /**
     * Returns formatted date based of elapsed time
     */
    fun getFormattedTime(timeInMillis: Long): String {
        val date = Date(timeInMillis * 1000L)

        return when {
            isToday(date) -> onlyTime.format(date)
            isYesterday(date) -> "Yesterday"
            else -> onlyDate.format(date)
        }
    }

    /**
     * Checks if date is yesterday
     */
    private fun isYesterday(date: Date): Boolean {
        return DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS)
    }

    /**
     * Checks if date is today
     */
    private fun isToday(date: Date): Boolean {
        return DateUtils.isToday(date.time)
    }

}