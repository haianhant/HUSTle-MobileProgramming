package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User roles in the application
 */
enum class UserRole {
    APPLICANT,
    HR
}

/**
 * User entity representing both Applicants and HR users
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val role: UserRole,
    val avatarUrl: String? = null,
    val headline: String? = null,
    val about: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
