package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Comment - Đại diện cho bảng "comments" trong Room Database
 * Lưu trữ các bình luận cho bài đăng trong Community
 * 
 * Mối quan hệ:
 * - Một Post có thể có nhiều Comment (1-N)
 * - Một User có thể viết nhiều Comment (1-N)
 */
@Entity(
    tableName = "comments",
    foreignKeys = [
        // Liên kết với bài đăng được bình luận
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE // Xóa Post sẽ xóa tất cả Comment
        ),
        // Liên kết với người viết bình luận
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE // Xóa User sẽ xóa tất cả Comment của user
        )
    ],
    indices = [Index("postId"), Index("authorId")]  // Index cho cả hai khóa ngoại
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,           // ID bình luận (tự động tăng)
    val postId: Long,           // ID bài đăng được bình luận (khóa ngoại)
    val authorId: Long,         // ID người viết bình luận (khóa ngoại)
    val content: String,        // Nội dung bình luận
    val createdAt: Long = System.currentTimeMillis() // Thời gian bình luận
)
