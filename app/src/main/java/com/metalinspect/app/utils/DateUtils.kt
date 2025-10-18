package com.metalinspect.app.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private val dateFormatDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormatDisplay = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormatDisplay = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val dateFormatExport = SimpleDateFormat(Constants.CSV_DATE_FORMAT, Locale.getDefault())
    
    fun formatDate(timestamp: Long): String {
        return dateFormatDisplay.format(Date(timestamp))
    }
    
    fun formatTime(timestamp: Long): String {
        return timeFormatDisplay.format(Date(timestamp))
    }
    
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormatDisplay.format(Date(timestamp))
    }
    
    fun formatForExport(timestamp: Long): String {
        return dateFormatExport.format(Date(timestamp))
    }
    
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
    
    fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    fun getEndOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    fun getDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return getStartOfDay(calendar.timeInMillis)
    }
    
    fun isToday(timestamp: Long): Boolean {
        val today = getStartOfDay(getCurrentTimestamp())
        val date = getStartOfDay(timestamp)
        return today == date
    }
    
    fun isYesterday(timestamp: Long): Boolean {
        val yesterday = getDaysAgo(1)
        val date = getStartOfDay(timestamp)
        return yesterday == date
    }
    
    fun getRelativeTimeString(timestamp: Long): String {
        return when {
            isToday(timestamp) -> "Today ${formatTime(timestamp)}"
            isYesterday(timestamp) -> "Yesterday ${formatTime(timestamp)}"
            else -> formatDateTime(timestamp)
        }
    }
}
