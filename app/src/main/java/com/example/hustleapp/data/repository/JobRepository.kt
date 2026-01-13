package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.ApplicationDao
import com.example.hustleapp.data.local.dao.JobDao
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.data.local.entity.JobStatus

/**
 * Repository for job-related data operations
 */
class JobRepository(
    private val jobDao: JobDao,
    private val applicationDao: ApplicationDao
) {
    // Job operations
    fun getAllOpenJobs(): LiveData<List<Job>> {
        return jobDao.getOpenJobs()
    }
    
    fun getAllJobs(): LiveData<List<Job>> {
        return jobDao.getAllJobs()
    }
    
    fun getJobsByHrId(hrUserId: Long): LiveData<List<Job>> {
        return jobDao.getJobsByHrId(hrUserId)
    }
    
    fun getJobByIdLiveData(jobId: Long): LiveData<Job?> {
        return jobDao.getJobByIdLiveData(jobId)
    }
    
    suspend fun getJobById(jobId: Long): Job? {
        return jobDao.getJobById(jobId)
    }
    
    suspend fun createJob(job: Job): Long {
        return jobDao.insert(job)
    }
    
    suspend fun updateJob(job: Job) {
        jobDao.update(job)
    }
    
    suspend fun deleteJob(job: Job) {
        jobDao.delete(job)
    }
    
    suspend fun updateJobStatus(jobId: Long, status: JobStatus) {
        jobDao.updateJobStatus(jobId, status)
    }
    
    suspend fun incrementViewCount(jobId: Long) {
        jobDao.incrementViewCount(jobId)
    }
    
    fun searchJobs(query: String): LiveData<List<Job>> {
        return jobDao.searchJobs(query)
    }
    
    // Application operations
    suspend fun applyForJob(application: Application): Long {
        return applicationDao.insert(application)
    }
    
    suspend fun hasApplied(jobId: Long, applicantId: Long): Boolean {
        return applicationDao.hasApplied(jobId, applicantId)
    }
    
    fun getApplicationsByJobId(jobId: Long): LiveData<List<Application>> {
        return applicationDao.getApplicationsByJobId(jobId)
    }
    
    fun getApplicationsByApplicantId(applicantId: Long): LiveData<List<Application>> {
        return applicationDao.getApplicationsByApplicantId(applicantId)
    }
    
    suspend fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus) {
        applicationDao.updateStatus(applicationId, status)
    }
    
    suspend fun getApplicationById(applicationId: Long): Application? {
        return applicationDao.getApplicationById(applicationId)
    }
    
    // Analytics
    suspend fun getJobCountByHr(hrUserId: Long): Int {
        return jobDao.getJobCountByHr(hrUserId)
    }
    
    suspend fun getTotalViewsByHr(hrUserId: Long): Int {
        return jobDao.getTotalViewsByHr(hrUserId) ?: 0
    }
    
    suspend fun getTotalApplicationsByHr(hrUserId: Long): Int {
        return applicationDao.getTotalApplicationsByHr(hrUserId)
    }
    
    suspend fun getApplicationCountByStatusAndHr(hrUserId: Long, status: ApplicationStatus): Int {
        return applicationDao.getApplicationCountByStatusAndHr(hrUserId, status)
    }
    
    suspend fun getApplicationCountByJob(jobId: Long): Int {
        return applicationDao.getApplicationCountByJob(jobId)
    }
    
    fun getAllApplicationsByHr(hrUserId: Long): LiveData<List<Application>> {
        return applicationDao.getAllApplicationsByHr(hrUserId)
    }
    
    suspend fun getApplicationByJobAndUser(jobId: Long, userId: Long): Application? {
        return applicationDao.getApplicationByJobAndApplicant(jobId, userId)
    }
}
