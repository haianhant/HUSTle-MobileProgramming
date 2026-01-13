package com.example.hustleapp.ui.applicant.jobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.Job
import kotlinx.coroutines.launch

/**
 * ViewModel for Jobs screen
 */
class JobsViewModel : ViewModel() {
    
    private val jobRepository = HUSTleApplication.instance.jobRepository
    private val sessionManager = HUSTleApplication.instance.sessionManager
    
    val jobs: LiveData<List<Job>> = jobRepository.getAllOpenJobs()
    
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery
    
    private val _filteredJobs = MutableLiveData<List<Job>>()
    val filteredJobs: LiveData<List<Job>> = _filteredJobs
    
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun searchJobs(query: String): LiveData<List<Job>> {
        return if (query.isBlank()) {
            jobs
        } else {
            jobRepository.searchJobs(query)
        }
    }
    
    fun applyForJob(jobId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId()
                
                // Check if already applied
                if (jobRepository.hasApplied(jobId, userId)) {
                    _message.value = "Bạn đã ứng tuyển công việc này rồi"
                    _isLoading.value = false
                    return@launch
                }
                
                val application = Application(
                    jobId = jobId,
                    applicantId = userId
                )
                jobRepository.applyForJob(application)
                _message.value = "Ứng tuyển thành công!"
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    suspend fun hasApplied(jobId: Long): Boolean {
        return jobRepository.hasApplied(jobId, sessionManager.getUserId())
    }
    
    fun incrementJobView(jobId: Long) {
        viewModelScope.launch {
            try {
                jobRepository.incrementViewCount(jobId)
            } catch (e: Exception) {
                // Ignore view count errors
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
}
