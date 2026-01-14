package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.CommentDao
import com.example.hustleapp.data.local.dao.PostDao
import com.example.hustleapp.data.local.entity.Comment
import com.example.hustleapp.data.local.entity.Post
import com.example.hustleapp.data.local.entity.PostLike

/**
 * Repository cho Community Feed (Bài đăng cộng đồng)
 * 
 * Chức năng chính:
 * - Quản lý bài đăng (tạo, sửa, xóa)
 * - Quản lý lượt thích (like/unlike)
 * - Quản lý bình luận
 */
class PostRepository(
    private val postDao: PostDao,       // DAO cho bảng posts và post_likes
    private val commentDao: CommentDao  // DAO cho bảng comments
) {
    // ==================== THAO TÁC BÀI ĐĂNG ====================
    
    /** Lấy tất cả bài đăng (cho Community Feed) */
    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getAllPosts()
    }
    
    /** Lấy bài đăng của một tác giả (hiển thị trên profile) */
    fun getPostsByAuthorId(authorId: Long): LiveData<List<Post>> {
        return postDao.getPostsByAuthorId(authorId)
    }
    
    /** Lấy chi tiết bài đăng (LiveData) */
    fun getPostByIdLiveData(postId: Long): LiveData<Post?> {
        return postDao.getPostByIdLiveData(postId)
    }
    
    /** Lấy chi tiết bài đăng (1 lần) */
    suspend fun getPostById(postId: Long): Post? {
        return postDao.getPostById(postId)
    }
    
    /** Tạo bài đăng mới */
    suspend fun createPost(post: Post): Long {
        return postDao.insert(post)
    }
    
    /** Cập nhật nội dung bài đăng */
    suspend fun updatePost(post: Post) {
        postDao.update(post)
    }
    
    /** Xóa bài đăng */
    suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }
    
    // ==================== THAO TÁC LIKE ====================
    
    /**
     * Thích bài đăng
     * - Thêm record vào post_likes
     * - Tăng likeCount trong posts
     */
    suspend fun likePost(postId: Long, userId: Long) {
        val postLike = PostLike(postId = postId, userId = userId)
        val result = postDao.insertLike(postLike)
        // Chỉ tăng count nếu insert thành công (-1 = đã like rồi)
        if (result != -1L) {
            postDao.incrementLikeCount(postId)
        }
    }
    
    /**
     * Bỏ thích bài đăng
     * - Xóa record khỏi post_likes
     * - Giảm likeCount trong posts
     */
    suspend fun unlikePost(postId: Long, userId: Long) {
        postDao.deleteLike(postId, userId)
        postDao.decrementLikeCount(postId)
    }
    
    /** Kiểm tra user đã thích bài này chưa (1 lần) */
    suspend fun hasUserLikedPost(postId: Long, userId: Long): Boolean {
        return postDao.hasUserLikedPost(postId, userId)
    }
    
    /** Kiểm tra user đã thích bài này chưa (LiveData - cập nhật UI realtime) */
    fun hasUserLikedPostLiveData(postId: Long, userId: Long): LiveData<Boolean> {
        return postDao.hasUserLikedPostLiveData(postId, userId)
    }
    
    /** Toggle like: Nếu đã like thì unlike, chưa like thì like */
    suspend fun toggleLike(postId: Long, userId: Long) {
        if (hasUserLikedPost(postId, userId)) {
            unlikePost(postId, userId)
        } else {
            likePost(postId, userId)
        }
    }
    
    // ==================== THAO TÁC BÌNH LUẬN ====================
    
    /** Lấy danh sách bình luận của bài đăng */
    fun getCommentsByPostId(postId: Long): LiveData<List<Comment>> {
        return commentDao.getCommentsByPostId(postId)
    }
    
    /**
     * Thêm bình luận mới
     * - Insert vào bảng comments
     * - Tăng commentCount trong posts
     */
    suspend fun addComment(comment: Comment): Long {
        val commentId = commentDao.insert(comment)
        if (commentId != -1L) {
            postDao.incrementCommentCount(comment.postId)
        }
        return commentId
    }
    
    /**
     * Xóa bình luận
     * - Delete khỏi bảng comments
     * - Giảm commentCount trong posts
     */
    suspend fun deleteComment(comment: Comment) {
        commentDao.delete(comment)
        postDao.decrementCommentCount(comment.postId)
    }
}
