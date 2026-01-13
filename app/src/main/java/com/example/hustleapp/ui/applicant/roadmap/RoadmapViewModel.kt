package com.example.hustleapp.ui.applicant.roadmap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustleapp.data.remote.GeminiService
import com.example.hustleapp.data.remote.RoadmapStepData
import kotlinx.coroutines.launch

/**
 * ViewModel for Roadmap screen with Gemini AI integration
 */
class RoadmapViewModel : ViewModel() {
    
    private val geminiService = GeminiService()
    
    // Input fields - exposed as MutableLiveData for two-way binding
    val currentRole = MutableLiveData("")
    val targetRole = MutableLiveData("")
    val currentSkills = MutableLiveData("")
    
    private val _yearsExperience = MutableLiveData(0)
    val yearsExperience: LiveData<Int> = _yearsExperience
    
    // Generated roadmap
    private val _roadmapSteps = MutableLiveData<List<RoadmapStepData>>(emptyList())
    val roadmapSteps: LiveData<List<RoadmapStepData>> = _roadmapSteps
    
    // UI State
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _showInputForm = MutableLiveData(true)
    val showInputForm: LiveData<Boolean> = _showInputForm
    
    fun setYearsExperience(value: Int) {
        _yearsExperience.value = value
    }
    
    fun generateRoadmap() {
        val current = currentRole.value ?: ""
        val target = targetRole.value ?: ""
        val skills = currentSkills.value ?: ""
        val years = _yearsExperience.value ?: 0
        
        // Validate input
        if (current.isBlank()) {
            _errorMessage.value = "Vui lòng nhập vị trí hiện tại"
            return
        }
        if (target.isBlank()) {
            _errorMessage.value = "Vui lòng nhập vị trí mục tiêu"
            return
        }
        
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            val result = geminiService.generateRoadmap(
                currentRole = current,
                targetRole = target,
                currentSkills = skills,
                yearsExperience = years
            )
            
            result.onSuccess { steps ->
                _roadmapSteps.value = steps
                _showInputForm.value = false
            }.onFailure { exception ->
                _errorMessage.value = "Lỗi: ${exception.message}"
            }
            
            _isLoading.value = false
        }
    }
    
    fun resetRoadmap() {
        _roadmapSteps.value = emptyList()
        _showInputForm.value = true
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
