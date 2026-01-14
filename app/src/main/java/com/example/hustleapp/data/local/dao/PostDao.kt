package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Post
import com.example.hustleapp.data.local.entity.PostLike

/**
 * DAO cho bảng Posts và PostLikes (Bài đăng cộng đồng)
 * 
 * Quản lý các bài đăng trong Community Feed:
 * - CRUD cho bài đăng
 * - Đếm và quản lý lượt thích
 * - Đếm bình luận
 */
@Dao
interface PostDao {
    
    // ==================== THAO TÁC CRUD CƠ BẢN ====================
    
    /** Thêm bài đăng mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long
    
    /** Cập nhật nội dung bài đăng */
    @Update
    suspend fun update(post: Post)
    
    /** Xóa bài đăng */
    @Delete
    suspend fun delete(post: Post)
    
    // ==================== TRUY VẤN BÀI ĐĂNG ====================
    
    /** Lấy bài đăng theo ID (1 lần) */
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?
    
    /** Lấy bài đăng theo ID (LiveData - cập nhật realtime) */
    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostByIdLiveData(postId: Long): LiveData<Post?>
    
    /** Lấy tất cả bài đăng, mới nhất trước (cho Community Feed) */
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): LiveData<List<Post>>
    
    /** Lấy bài đăng của một tác giả (hiển thị trên profile) */
    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthorId(authorId: Long): LiveData<List<Post>>
    
    // ==================== QUẢN LÝ LƯỢT THÍCH ====================
    
    /** Tăng số lượt thích (+1) */
    @Query("UPDATE posts SET likeCount = likeCount + 1 WHERE id = :postId")
    suspend fun incrementLikeCount(postId: Long)
    
    /** Giảm số lượt thích (-1), không xuống dưới 0 */
    @Query("UPDATE posts SET likeCount = likeCount - 1 WHERE id = :postId AND likeCount > 0")
    suspend fun decrementLikeCount(postId: Long)
    
    // ==================== QUẢN LÝ SỐ BÌNH LUẬN ====================
    
    /** Tăng số bình luận (+1) */
    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :postId")
    suspend fun incrementCommentCount(postId: Long)
    
    /** Giảm số bình luận (-1), không xuống dưới 0 */
    @Query("UPDATE posts SET commentCount = commentCount - 1 WHERE id = :postId AND commentCount > 0")
    suspend fun decrementCommentCount(postId: Long)
    
    // ==================== POSTLIKE OPERATIONS ====================
    // Quản lý bảng post_likes để theo dõi ai đã thích bài nào
    
    /** Thêm lượt thích (IGNORE nếu đã thích rồi) */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(postLike: PostLike): Long
    
    /** Bỏ thích bài đăng */
    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deleteLike(postId: Long, userId: Long)
    
    /** Kiểm tra user đã thích bài này chưa (1 lần) */
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasUserLikedPost(postId: Long, userId: Long): Boolean
    
    /** Kiểm tra user đã thích bài này chưa (LiveData - cập nhật UI realtime) */
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    fun hasUserLikedPostLiveData(postId: Long, userId: Long): LiveData<Boolean>
}
