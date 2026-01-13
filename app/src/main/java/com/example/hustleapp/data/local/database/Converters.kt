package com.example.hustleapp.data.local.database

import androidx.room.TypeConverter
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.data.local.entity.JobStatus
import com.example.hustleapp.data.local.entity.UserRole

/**
 * Type converters for Room database
 */
class Converters {
    
    // UserRole converters
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name
    
    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)
    
    // JobStatus converters
    @TypeConverter
    fun fromJobStatus(status: JobStatus): String = status.name
    
    @TypeConverter
    fun toJobStatus(value: String): JobStatus = JobStatus.valueOf(value)
    
    // ApplicationStatus converters
    @TypeConverter
    fun fromApplicationStatus(status: ApplicationStatus): String = status.name
    
    @TypeConverter
    fun toApplicationStatus(value: String): ApplicationStatus = ApplicationStatus.valueOf(value)
}
