package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity RoadmapStep - Đại diện cho bảng "roadmap_steps" trong Room Database
 * Lưu trữ các bước trong lộ trình phát triển nghề nghiệp
 * 
 * Tính năng Roadmap:
 * - Sử dụng AI (Gemini) để tạo lộ trình phát triển cá nhân
 * - Mỗi roadmap gồm nhiều bước tuần tự (stepNumber)
 * - Người dùng có thể đánh dấu các bước đã hoàn thành
 * 
 * Ví dụ: Lộ trình từ "Junior Developer" -> "Senior Developer"
 * - Bước 1: Học design patterns
 * - Bước 2: Học architecture
 * - Bước 3: Lead dự án nhỏ
 */
@Entity(
    tableName = "roadmap_steps",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // Xóa User sẽ xóa roadmap
        )
    ],
    indices = [Index("userId")]  // Index để tăng tốc truy vấn
)
data class RoadmapStep(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                   // ID bước roadmap (tự động tăng)
    val userId: Long,                   // ID người dùng sở hữu (khóa ngoại)
    val targetRole: String,             // Vị trí mục tiêu (VD: "Senior Developer")
    val stepNumber: Int,                // Số thứ tự bước (1, 2, 3...)
    val title: String,                  // Tiêu đề bước
    val description: String? = null,    // Mô tả chi tiết cách thực hiện
    val isCompleted: Boolean = false,   // Đánh dấu đã hoàn thành hay chưa
    val createdAt: Long = System.currentTimeMillis() // Thời gian tạo
)
