package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Education - Đại diện cho bảng "education" trong Room Database
 * Lưu trữ thông tin học vấn của người dùng (Applicant)
 * 
 * Mục đích:
 * - Thể hiện trình độ học vấn của ứng viên
 * - Hiển thị trên profile cho HR đánh giá nền tảng học vấn
 * 
 * Mối quan hệ:
 * - Một User có thể có nhiều Education (1-N)
 */
@Entity(
    tableName = "education",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // Xóa User sẽ xóa tất cả học vấn
        )
    ],
    indices = [Index("userId")]  // Index để tăng tốc truy vấn
)
data class Education(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                   // ID học vấn (tự động tăng)
    val userId: Long,                   // ID người dùng sở hữu (khóa ngoại)
    val degree: String,                 // Bằng cấp (VD: "Cử nhân Khoa học Máy tính")
    val school: String,                 // Tên trường học
    val startDate: Long,                // Ngày bắt đầu học (timestamp)
    val endDate: Long? = null,          // Ngày tốt nghiệp (null = đang học)
    val description: String? = null     // Mô tả thêm về quá trình học
)
