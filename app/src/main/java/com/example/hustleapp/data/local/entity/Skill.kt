package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Skill - Đại diện cho bảng "skills" trong Room Database
 * Lưu trữ các kỹ năng chuyên môn của người dùng
 * 
 * Mối quan hệ:
 * - Một User có thể có nhiều Skill (1-N)
 * - Mỗi Skill thuộc về một User duy nhất
 * 
 * Ví dụ kỹ năng: "Java", "Kotlin", "Android Development", "UI/UX Design"
 */
@Entity(
    tableName = "skills",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],       // Cột khóa chính bảng cha
            childColumns = ["userId"],    // Cột khóa ngoại bảng này
            onDelete = ForeignKey.CASCADE // Xóa User sẽ xóa tất cả Skill của user
        )
    ],
    indices = [Index("userId")]  // Index để tăng tốc truy vấn theo userId
)
data class Skill(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,       // ID kỹ năng (tự động tăng)
    val userId: Long,       // ID người dùng sở hữu kỹ năng (khóa ngoại)
    val name: String        // Tên kỹ năng (ví dụ: "Kotlin", "Android")
)
