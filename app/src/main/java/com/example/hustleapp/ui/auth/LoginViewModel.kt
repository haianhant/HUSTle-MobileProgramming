package com.example.hustleapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.User
import com.example.hustleapp.data.local.entity.UserRole
import com.example.hustleapp.utils.ValidationUtils
import kotlinx.coroutines.launch

/**
 * ViewModel for Login screen
 */
class LoginViewModel : ViewModel() {
    
    private val userRepository = HUSTleApplication.instance.userRepository
    private val sessionManager = HUSTleApplication.instance.sessionManager
    
    // UI State
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _loginSuccess = MutableLiveData<User?>()
    val loginSuccess: LiveData<User?> = _loginSuccess
    
    fun setEmail(value: String) {
        if (email.value != value) {
            email.value = value
        }
    }
    
    fun setPassword(value: String) {
        if (password.value != value) {
            password.value = value
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun login() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        
        // Validate input
        val validation = ValidationUtils.validateLoginForm(emailValue, passwordValue)
        if (!validation.isValid) {
            _errorMessage.value = validation.errorMessage
            return
        }
        
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                val user = userRepository.login(emailValue, passwordValue)
                if (user != null) {
                    // Save session
                    sessionManager.saveSession(user.id, user.email, user.name, user.role)
                    _loginSuccess.value = user
                } else {
                    _errorMessage.value = "Email hoặc mật khẩu không đúng"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Check if user is already logged in
     */
    fun checkExistingSession(): Boolean {
        return sessionManager.isLoggedIn()
    }
    
    fun getCurrentUserRole(): UserRole? {
        return sessionManager.getUserRole()
    }
}
