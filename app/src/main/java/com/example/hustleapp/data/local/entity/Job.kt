package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Job status enum
 */
enum class JobStatus {
    OPEN,
    CLOSED
}

/**
 * Job entity representing job postings created by HR
 */
@Entity(
    tableName = "jobs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["hrUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("hrUserId")]
)
data class Job(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hrUserId: Long,
    val title: String,
    val company: String,
    val salary: String,
    val location: String,
    val description: String,
    val requirements: String,
    val status: JobStatus = JobStatus.OPEN,
    val viewCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
