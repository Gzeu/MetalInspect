package com.metalinspect.app.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private val displayDateFormat = SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT, Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat(Constants.DISPLAY_TIME_FORMAT, Locale.getDefault())
    private val exportDateFormat = SimpleDateFormat(Constants.CSV_DATE_FORMAT, Locale.getDefault())
    private val filenameDateFormat = SimpleDateFormat(Constants.EXPORT_DATE_FORMAT, Locale.getDefault())
    
    fun formatDateTime(timestamp: Long): String {
        return displayDateFormat.format(Date(timestamp))
    }
    
    fun formatTime(timestamp: Long): String {
        return displayTimeFormat.format(Date(timestamp))
    }
    
    fun formatForExport(timestamp: Long): String {
        return exportDateFormat.format(Date(timestamp))
    }
    
    fun formatForFilename(timestamp: Long): String {
        return filenameDateFormat.format(Date(timestamp))
    }
    
    fun formatRelativeTime(timestamp: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }
    
    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val tomorrow = Calendar.getInstance().apply {
            timeInMillis = today.timeInMillis
            add(Calendar.DAY_OF_YEAR, 1)
        }
        
        return timestamp >= today.timeInMillis && timestamp < tomorrow.timeInMillis
    }
    
    fun isYesterday(timestamp: Long): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        return timestamp >= yesterday.timeInMillis && timestamp < today.timeInMillis
    }
    
    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    
    fun getEndOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }
    
    fun getDaysDifference(startTimestamp: Long, endTimestamp: Long): Int {
        val diffInMs = endTimestamp - startTimestamp
        return (diffInMs / (24 * 60 * 60 * 1000)).toInt()
    }
}