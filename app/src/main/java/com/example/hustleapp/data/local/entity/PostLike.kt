package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity PostLike - Đại diện cho bảng "post_likes" trong Room Database
 * Theo dõi ai đã thích bài đăng nào trong Community
 * 
 * Mục đích:
 * - Đảm bảo mỗi user chỉ có thể thích 1 bài đăng 1 lần (unique constraint)
 * - Cho phép hủy thích bằng cách xóa record
 * - Hỗ trợ truy vấn danh sách người đã thích bài đăng
 * 
 * Index unique (postId, userId):
 * - Ngăn chặn một user thích cùng một bài đăng nhiều lần
 */
@Entity(
    tableName = "post_likes",
    foreignKeys = [
        // Liên kết với bài đăng được thích
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE  // Xóa Post sẽ xóa tất cả like
        ),
        // Liên kết với người thích
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // Xóa User sẽ xóa tất cả like của user
        )
    ],
    // Index unique để đảm bảo 1 user chỉ like 1 post 1 lần
    indices = [Index("postId"), Index("userId"), Index(value = ["postId", "userId"], unique = true)]
)
data class PostLike(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,           // ID lượt thích (tự động tăng)
    val postId: Long,           // ID bài đăng được thích (khóa ngoại)
    val userId: Long,           // ID người thích (khóa ngoại)
    val createdAt: Long = System.currentTimeMillis() // Thời gian thích
)
