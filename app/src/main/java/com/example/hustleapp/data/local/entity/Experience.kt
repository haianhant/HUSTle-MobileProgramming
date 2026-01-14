package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Experience - Đại diện cho bảng "experiences" trong Room Database
 * Lưu trữ kinh nghiệm làm việc của người dùng (Applicant)
 * 
 * Mục đích:
 * - Thể hiện lịch sử công việc của ứng viên
 * - Hiển thị trên profile cho HR xem khi đánh giá ứng viên
 * 
 * Mối quan hệ:
 * - Một User có thể có nhiều Experience (1-N)
 */
@Entity(
    tableName = "experiences",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // Xóa User sẽ xóa tất cả kinh nghiệm
        )
    ],
    indices = [Index("userId")]  // Index để tăng tốc truy vấn
)
data class Experience(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                   // ID kinh nghiệm (tự động tăng)
    val userId: Long,                   // ID người dùng sở hữu (khóa ngoại)
    val title: String,                  // Chức danh công việc
    val company: String,                // Tên công ty
    val startDate: Long,                // Ngày bắt đầu (timestamp)
    val endDate: Long? = null,          // Ngày kết thúc (null = đang làm việc)
    val description: String? = null     // Mô tả công việc chi tiết
)
