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
 * ViewModel for Register screen
 */
class RegisterViewModel : ViewModel() {
    
    private val userRepository = HUSTleApplication.instance.userRepository
    private val sessionManager = HUSTleApplication.instance.sessionManager
    
    // UI State
    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")
    
    private val _selectedRole = MutableLiveData(UserRole.APPLICANT)
    val selectedRole: LiveData<UserRole> = _selectedRole
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _registerSuccess = MutableLiveData<User?>()
    val registerSuccess: LiveData<User?> = _registerSuccess
    
    fun setName(value: String) {
        if (name.value != value) name.value = value
    }
    
    fun setEmail(value: String) {
        if (email.value != value) email.value = value
    }
    
    fun setPassword(value: String) {
        if (password.value != value) password.value = value
    }
    
    fun setConfirmPassword(value: String) {
        if (confirmPassword.value != value) confirmPassword.value = value
    }
    
    fun setRole(role: UserRole) {
        _selectedRole.value = role
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun register() {
        val nameValue = name.value ?: ""
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""
        val roleValue = _selectedRole.value ?: UserRole.APPLICANT
        
        // Validate input
        val validation = ValidationUtils.validateRegisterForm(
            nameValue, emailValue, passwordValue, confirmPasswordValue
        )
        if (!validation.isValid) {
            _errorMessage.value = validation.errorMessage
            return
        }
        
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                // Check if email already exists
                if (userRepository.isEmailExists(emailValue)) {
                    _errorMessage.value = "Email đã được sử dụng"
                    _isLoading.value = false
                    return@launch
                }
                
                // Create user
                val user = User(
                    email = emailValue,
                    password = passwordValue,
                    name = nameValue,
                    role = roleValue
                )
                
                val userId = userRepository.register(user)
                val createdUser = user.copy(id = userId)
                
                // Save session
                sessionManager.saveSession(userId, emailValue, nameValue, roleValue)
                _registerSuccess.value = createdUser
                
            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
