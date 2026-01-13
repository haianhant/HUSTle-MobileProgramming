package com.example.hustleapp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility object for date formatting
 */
object DateUtils {
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val monthYearFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    private val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("vi", "VN"))
    private val timeAgoFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    
    /**
     * Format timestamp to dd/MM/yyyy
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to MM/yyyy
     */
    fun formatMonthYear(timestamp: Long): String {
        return monthYearFormat.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to full Vietnamese date
     */
    fun formatFullDate(timestamp: Long): String {
        return fullDateFormat.format(Date(timestamp))
    }
    
    /**
     * Format date range for experience/education
     */
    fun formatDateRange(startDate: Long, endDate: Long?): String {
        val start = formatMonthYear(startDate)
        val end = endDate?.let { formatMonthYear(it) } ?: "Hiện tại"
        return "$start - $end"
    }
    
    /**
     * Get relative time string (e.g., "2 giờ trước", "3 ngày trước")
     */
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        
        return when {
            seconds < 60 -> "Vừa xong"
            minutes < 60 -> "$minutes phút trước"
            hours < 24 -> "$hours giờ trước"
            days < 7 -> "$days ngày trước"
            weeks < 4 -> "$weeks tuần trước"
            months < 12 -> "$months tháng trước"
            else -> formatDate(timestamp)
        }
    }
    
    /**
     * Parse date string to timestamp
     */
    fun parseDate(dateString: String): Long? {
        return try {
            dateFormat.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Get current timestamp
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}
