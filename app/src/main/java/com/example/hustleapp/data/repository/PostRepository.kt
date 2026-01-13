package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.CommentDao
import com.example.hustleapp.data.local.dao.PostDao
import com.example.hustleapp.data.local.entity.Comment
import com.example.hustleapp.data.local.entity.Post
import com.example.hustleapp.data.local.entity.PostLike

/**
 * Repository for social feed data operations
 */
class PostRepository(
    private val postDao: PostDao,
    private val commentDao: CommentDao
) {
    // Post operations
    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getAllPosts()
    }
    
    fun getPostsByAuthorId(authorId: Long): LiveData<List<Post>> {
        return postDao.getPostsByAuthorId(authorId)
    }
    
    fun getPostByIdLiveData(postId: Long): LiveData<Post?> {
        return postDao.getPostByIdLiveData(postId)
    }
    
    suspend fun getPostById(postId: Long): Post? {
        return postDao.getPostById(postId)
    }
    
    suspend fun createPost(post: Post): Long {
        return postDao.insert(post)
    }
    
    suspend fun updatePost(post: Post) {
        postDao.update(post)
    }
    
    suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }
    
    // Like operations
    suspend fun likePost(postId: Long, userId: Long) {
        val postLike = PostLike(postId = postId, userId = userId)
        val result = postDao.insertLike(postLike)
        if (result != -1L) {
            postDao.incrementLikeCount(postId)
        }
    }
    
    suspend fun unlikePost(postId: Long, userId: Long) {
        postDao.deleteLike(postId, userId)
        postDao.decrementLikeCount(postId)
    }
    
    suspend fun hasUserLikedPost(postId: Long, userId: Long): Boolean {
        return postDao.hasUserLikedPost(postId, userId)
    }
    
    fun hasUserLikedPostLiveData(postId: Long, userId: Long): LiveData<Boolean> {
        return postDao.hasUserLikedPostLiveData(postId, userId)
    }
    
    suspend fun toggleLike(postId: Long, userId: Long) {
        if (hasUserLikedPost(postId, userId)) {
            unlikePost(postId, userId)
        } else {
            likePost(postId, userId)
        }
    }
    
    // Comment operations
    fun getCommentsByPostId(postId: Long): LiveData<List<Comment>> {
        return commentDao.getCommentsByPostId(postId)
    }
    
    suspend fun addComment(comment: Comment): Long {
        val commentId = commentDao.insert(comment)
        if (commentId != -1L) {
            postDao.incrementCommentCount(comment.postId)
        }
        return commentId
    }
    
    suspend fun deleteComment(comment: Comment) {
        commentDao.delete(comment)
        postDao.decrementCommentCount(comment.postId)
    }
}
