package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Comment

/**
 * DAO cho bảng Comments (Bình luận bài đăng)
 * 
 * Quản lý các bình luận trong Community Feed
 */
@Dao
interface CommentDao {
    
    /** Thêm bình luận mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment): Long
    
    /** Cập nhật nội dung bình luận */
    @Update
    suspend fun update(comment: Comment)
    
    /** Xóa bình luận */
    @Delete
    suspend fun delete(comment: Comment)
    
    /** Lấy bình luận theo ID */
    @Query("SELECT * FROM comments WHERE id = :commentId")
    suspend fun getCommentById(commentId: Long): Comment?
    
    /** Lấy tất cả bình luận của một bài đăng (LiveData - cập nhật realtime) */
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt ASC")
    fun getCommentsByPostId(postId: Long): LiveData<List<Comment>>
    
    /** Lấy bình luận của bài đăng (1 lần, không LiveData) */
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt ASC")
    suspend fun getCommentsByPostIdSync(postId: Long): List<Comment>
    
    /** Đếm số bình luận của bài đăng */
    @Query("SELECT COUNT(*) FROM comments WHERE postId = :postId")
    suspend fun getCommentCountByPostId(postId: Long): Int
}
