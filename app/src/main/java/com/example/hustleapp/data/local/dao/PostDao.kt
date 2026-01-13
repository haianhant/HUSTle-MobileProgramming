package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Post
import com.example.hustleapp.data.local.entity.PostLike

@Dao
interface PostDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long
    
    @Update
    suspend fun update(post: Post)
    
    @Delete
    suspend fun delete(post: Post)
    
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?
    
    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostByIdLiveData(postId: Long): LiveData<Post?>
    
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): LiveData<List<Post>>
    
    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthorId(authorId: Long): LiveData<List<Post>>
    
    @Query("UPDATE posts SET likeCount = likeCount + 1 WHERE id = :postId")
    suspend fun incrementLikeCount(postId: Long)
    
    @Query("UPDATE posts SET likeCount = likeCount - 1 WHERE id = :postId AND likeCount > 0")
    suspend fun decrementLikeCount(postId: Long)
    
    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :postId")
    suspend fun incrementCommentCount(postId: Long)
    
    @Query("UPDATE posts SET commentCount = commentCount - 1 WHERE id = :postId AND commentCount > 0")
    suspend fun decrementCommentCount(postId: Long)
    
    // PostLike operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(postLike: PostLike): Long
    
    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deleteLike(postId: Long, userId: Long)
    
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasUserLikedPost(postId: Long, userId: Long): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    fun hasUserLikedPostLiveData(postId: Long, userId: Long): LiveData<Boolean>
}
