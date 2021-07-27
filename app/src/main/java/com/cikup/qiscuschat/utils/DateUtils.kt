package com.cikup.qiscuschat.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private var fullDateFormat: DateFormat? = null
    fun toFullDate(date: Date?): String {
        return fullDateFormat!!.format(date)
    }

    fun getLastMessageTimestamp(utcDate: Date?): String? {
        return if (utcDate != null) {
            val todayCalendar = Calendar.getInstance()
            val localCalendar = Calendar.getInstance()
            localCalendar.time = utcDate
            if (getDateStringFromDate(todayCalendar.time)
                == getDateStringFromDate(localCalendar.time)
            ) {
                getTimeStringFromDate(utcDate)
            } else if (todayCalendar[Calendar.DATE] - localCalendar[Calendar.DATE] == 1) {
                "Yesterday"
            } else {
                getDateStringFromDate(utcDate)
            }
        } else {
            null
        }
    }

    fun getTimeStringFromDate(date: Date?): String {
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.US)
        return dateFormat.format(date)
    }

    fun getDateStringFromDate(date: Date?): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return dateFormat.format(date)
    }

    fun getDateStringFromDateTimeline(date: Date?): String {
        val day: DateFormat = SimpleDateFormat("dd", Locale.US)
        val month1: DateFormat = SimpleDateFormat("MM", Locale.US)
        val years: DateFormat = SimpleDateFormat("yyyy", Locale.US)
        val dayText = day.format(date)
        val month = month1.format(date)
        var monthText = ""
        if (month == "01") {
            monthText = "Januari"
        } else if (month == "02") {
            monthText = "Febuari"
        } else if (month == "03") {
            monthText = "Maret"
        } else if (month == "04") {
            monthText = "April"
        } else if (month == "05") {
            monthText = "Mei"
        } else if (month == "06") {
            monthText = "Juni"
        } else if (month == "07") {
            monthText = "July"
        } else if (month == "08") {
            monthText = "Agustus"
        } else if (month == "09") {
            monthText = "September"
        } else if (month == "10") {
            monthText = "Oktober"
        } else if (month == "11") {
            monthText = "November"
        } else if (month == "12") {
            monthText = "Desember"
        }
        val yearsText = years.format(date)
        val time = getTimeStringFromDate(date)
        return "$dayText $monthText $yearsText"
    }

    init {
        fullDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy")
    }
}