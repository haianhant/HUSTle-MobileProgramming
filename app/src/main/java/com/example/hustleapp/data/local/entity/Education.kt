package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Education entity for user's educational background
 */
@Entity(
    tableName = "education",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Education(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val degree: String,
    val school: String,
    val startDate: Long,
    val endDate: Long? = null,
    val description: String? = null
)
