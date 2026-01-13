package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * RoadmapStep entity for career development steps
 */
@Entity(
    tableName = "roadmap_steps",
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
data class RoadmapStep(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val targetRole: String,
    val stepNumber: Int,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
