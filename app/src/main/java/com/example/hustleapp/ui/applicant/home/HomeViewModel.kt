package com.example.hustleapp.ui.applicant.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Post
import kotlinx.coroutines.launch

/**
 * ViewModel for Home/Social Feed screen
 */
class HomeViewModel : ViewModel() {
    
    private val postRepository = HUSTleApplication.instance.postRepository
    private val sessionManager = HUSTleApplication.instance.sessionManager
    
    val posts: LiveData<List<Post>> = postRepository.getAllPosts()
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    
    fun toggleLike(postId: Long) {
        viewModelScope.launch {
            try {
                postRepository.toggleLike(postId, sessionManager.getUserId())
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    suspend fun hasLikedPost(postId: Long): Boolean {
        return postRepository.hasUserLikedPost(postId, sessionManager.getUserId())
    }
    
    fun createPost(content: String) {
        if (content.isBlank()) {
            _message.value = "Nội dung không được để trống"
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val post = Post(
                    authorId = sessionManager.getUserId(),
                    content = content
                )
                postRepository.createPost(post)
                _message.value = "Đã đăng bài viết"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deletePost(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(post)
                _message.value = "Đã xóa bài viết"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
}
