package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Post - Đại diện cho bảng "posts" trong Room Database
 * Lưu trữ các bài đăng trên mạng xã hội/cộng đồng của ứng dụng
 * 
 * Tính năng Community Feed:
 * - Người dùng có thể đăng bài chia sẻ kinh nghiệm, thông tin
 * - Hỗ trợ đính kèm hình ảnh
 * - Đếm số lượt thích và bình luận
 */
@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],     // Người đăng bài
            onDelete = ForeignKey.CASCADE    // Xóa User sẽ xóa tất cả bài đăng
        )
    ],
    indices = [Index("authorId")]  // Index để tăng tốc truy vấn theo tác giả
)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,               // ID bài đăng (tự động tăng)
    val authorId: Long,             // ID tác giả bài đăng (khóa ngoại)
    val content: String,            // Nội dung bài đăng
    val imageUrl: String? = null,   // URL hình ảnh đính kèm (tùy chọn)
    val likeCount: Int = 0,         // Số lượt thích
    val commentCount: Int = 0,      // Số bình luận
    val createdAt: Long = System.currentTimeMillis() // Thời gian đăng bài
)
