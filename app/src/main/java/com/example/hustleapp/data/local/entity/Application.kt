package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Application status enum
 */
enum class ApplicationStatus {
    PENDING,
    SHORTLISTED,
    REJECTED
}

/**
 * Application entity representing job applications from Applicants
 */
@Entity(
    tableName = "applications",
    foreignKeys = [
        ForeignKey(
            entity = Job::class,
            parentColumns = ["id"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["applicantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("jobId"), Index("applicantId")]
)
data class Application(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val jobId: Long,
    val applicantId: Long,
    val status: ApplicationStatus = ApplicationStatus.PENDING,
    val cvUrl: String? = null,
    val coverLetter: String? = null,
    val appliedAt: Long = System.currentTimeMillis()
)
