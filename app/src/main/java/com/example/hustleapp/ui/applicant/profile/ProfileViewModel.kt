package com.example.hustleapp.ui.applicant.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Education
import com.example.hustleapp.data.local.entity.Experience
import com.example.hustleapp.data.local.entity.Skill
import com.example.hustleapp.data.local.entity.User
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile screen
 */
class ProfileViewModel : ViewModel() {
    
    private val userRepository = HUSTleApplication.instance.userRepository
    private val sessionManager = HUSTleApplication.instance.sessionManager
    
    private val userId = sessionManager.getUserId()
    
    // User data
    val user: LiveData<User?> = userRepository.getUserByIdLiveData(userId)
    val skills: LiveData<List<Skill>> = userRepository.getSkillsByUserId(userId)
    val experiences: LiveData<List<Experience>> = userRepository.getExperiencesByUserId(userId)
    val education: LiveData<List<Education>> = userRepository.getEducationByUserId(userId)
    
    // Edit mode
    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> = _isEditMode
    
    // Edit fields
    private val _editName = MutableLiveData("")
    val editName: LiveData<String> = _editName
    
    private val _editHeadline = MutableLiveData("")
    val editHeadline: LiveData<String> = _editHeadline
    
    private val _editAbout = MutableLiveData("")
    val editAbout: LiveData<String> = _editAbout
    
    // Loading state
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Messages
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    
    fun enterEditMode() {
        user.value?.let { currentUser ->
            _editName.value = currentUser.name
            _editHeadline.value = currentUser.headline ?: ""
            _editAbout.value = currentUser.about ?: ""
        }
        _isEditMode.value = true
    }
    
    fun cancelEditMode() {
        _isEditMode.value = false
    }
    
    fun setEditName(value: String) {
        _editName.value = value
    }
    
    fun setEditHeadline(value: String) {
        _editHeadline.value = value
    }
    
    fun setEditAbout(value: String) {
        _editAbout.value = value
    }
    
    fun saveProfile() {
        val currentUser = user.value ?: return
        val name = _editName.value ?: ""
        
        if (name.isBlank()) {
            _message.value = "Vui lòng nhập họ tên"
            return
        }
        
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val updatedUser = currentUser.copy(
                    name = name,
                    headline = _editHeadline.value,
                    about = _editAbout.value
                )
                userRepository.updateUser(updatedUser)
                _isEditMode.value = false
                _message.value = "Đã cập nhật hồ sơ"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addSkill(skillName: String) {
        if (skillName.isBlank()) return
        
        viewModelScope.launch {
            try {
                val skill = Skill(userId = userId, name = skillName.trim())
                userRepository.addSkill(skill)
            } catch (e: Exception) {
                _message.value = "Lỗi thêm kỹ năng: ${e.message}"
            }
        }
    }
    
    fun deleteSkill(skill: Skill) {
        viewModelScope.launch {
            try {
                userRepository.deleteSkill(skill)
            } catch (e: Exception) {
                _message.value = "Lỗi xóa kỹ năng: ${e.message}"
            }
        }
    }
    
    fun addExperience(experience: Experience) {
        viewModelScope.launch {
            try {
                userRepository.addExperience(experience)
                _message.value = "Đã thêm kinh nghiệm"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun updateExperience(experience: Experience) {
        viewModelScope.launch {
            try {
                userRepository.updateExperience(experience)
                _message.value = "Đã cập nhật kinh nghiệm"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun deleteExperience(experience: Experience) {
        viewModelScope.launch {
            try {
                userRepository.deleteExperience(experience)
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun addEducation(education: Education) {
        viewModelScope.launch {
            try {
                userRepository.addEducation(education)
                _message.value = "Đã thêm học vấn"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun updateEducation(education: Education) {
        viewModelScope.launch {
            try {
                userRepository.updateEducation(education)
                _message.value = "Đã cập nhật học vấn"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun deleteEducation(education: Education) {
        viewModelScope.launch {
            try {
                userRepository.deleteEducation(education)
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            }
        }
    }
    
    fun logout() {
        sessionManager.clearSession()
    }
    
    fun clearMessage() {
        _message.value = null
    }
}
