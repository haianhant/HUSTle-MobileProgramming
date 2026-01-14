package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.ApplicationDao
import com.example.hustleapp.data.local.dao.JobDao
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.data.local.entity.JobStatus

/**
 * Repository cho các thao tác liên quan đến Job (Tin tuyển dụng)
 * 
 * Chức năng chính:
 * - Quản lý tin tuyển dụng (CRUD)
 * - Quản lý đơn ứng tuyển
 * - Thống kê analytics cho HR
 * 
 * Sử dụng bởi:
 * - Applicant: xem tin, tìm kiếm, ứng tuyển
 * - HR: đăng tin, xem đơn, thống kê
 */
class JobRepository(
    private val jobDao: JobDao,              // DAO cho bảng jobs
    private val applicationDao: ApplicationDao // DAO cho bảng applications
) {
    // ==================== THAO TÁC TIN TUYỂN DỤNG ====================
    
    /** Lấy các tin đang mở (cho Applicant xem danh sách việc) */
    fun getAllOpenJobs(): LiveData<List<Job>> {
        return jobDao.getOpenJobs()
    }
    
    /** Lấy tất cả tin (bao gồm đã đóng) */
    fun getAllJobs(): LiveData<List<Job>> {
        return jobDao.getAllJobs()
    }
    
    /** Lấy tin của một HR cụ thể (cho HR quản lý tin) */
    fun getJobsByHrId(hrUserId: Long): LiveData<List<Job>> {
        return jobDao.getJobsByHrId(hrUserId)
    }
    
    /** Lấy chi tiết tin (LiveData) */
    fun getJobByIdLiveData(jobId: Long): LiveData<Job?> {
        return jobDao.getJobByIdLiveData(jobId)
    }
    
    /** Lấy chi tiết tin (1 lần) */
    suspend fun getJobById(jobId: Long): Job? {
        return jobDao.getJobById(jobId)
    }
    
    /** HR tạo tin tuyển dụng mới */
    suspend fun createJob(job: Job): Long {
        return jobDao.insert(job)
    }
    
    /** HR cập nhật tin tuyển dụng */
    suspend fun updateJob(job: Job) {
        jobDao.update(job)
    }
    
    /** HR xóa tin tuyển dụng */
    suspend fun deleteJob(job: Job) {
        jobDao.delete(job)
    }
    
    /** HR đóng/mở tin tuyển dụng */
    suspend fun updateJobStatus(jobId: Long, status: JobStatus) {
        jobDao.updateJobStatus(jobId, status)
    }
    
    /** Tăng lượt xem khi Applicant xem chi tiết tin */
    suspend fun incrementViewCount(jobId: Long) {
        jobDao.incrementViewCount(jobId)
    }
    
    /** Applicant tìm kiếm việc làm */
    fun searchJobs(query: String): LiveData<List<Job>> {
        return jobDao.searchJobs(query)
    }
    
    // ==================== THAO TÁC ĐƠN ỨNG TUYỂN ====================
    
    /** Applicant nộp đơn ứng tuyển */
    suspend fun applyForJob(application: Application): Long {
        return applicationDao.insert(application)
    }
    
    /** Kiểm tra Applicant đã ứng tuyển công việc này chưa */
    suspend fun hasApplied(jobId: Long, applicantId: Long): Boolean {
        return applicationDao.hasApplied(jobId, applicantId)
    }
    
    /** HR xem danh sách đơn ứng tuyển cho một tin */
    fun getApplicationsByJobId(jobId: Long): LiveData<List<Application>> {
        return applicationDao.getApplicationsByJobId(jobId)
    }
    
    /** Applicant xem lịch sử ứng tuyển */
    fun getApplicationsByApplicantId(applicantId: Long): LiveData<List<Application>> {
        return applicationDao.getApplicationsByApplicantId(applicantId)
    }
    
    /** HR cập nhật trạng thái đơn (PENDING -> SHORTLISTED/REJECTED) */
    suspend fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus) {
        applicationDao.updateStatus(applicationId, status)
    }
    
    /** Lấy chi tiết đơn ứng tuyển */
    suspend fun getApplicationById(applicationId: Long): Application? {
        return applicationDao.getApplicationById(applicationId)
    }
    
    // ==================== THỐNG KÊ ANALYTICS CHO HR ====================
    
    /** Đếm số tin của HR */
    suspend fun getJobCountByHr(hrUserId: Long): Int {
        return jobDao.getJobCountByHr(hrUserId)
    }
    
    /** Tổng lượt xem tất cả tin của HR */
    suspend fun getTotalViewsByHr(hrUserId: Long): Int {
        return jobDao.getTotalViewsByHr(hrUserId) ?: 0
    }
    
    /** Tổng số đơn ứng tuyển cho các tin của HR */
    suspend fun getTotalApplicationsByHr(hrUserId: Long): Int {
        return applicationDao.getTotalApplicationsByHr(hrUserId)
    }
    
    /** Đếm đơn theo trạng thái cho HR */
    suspend fun getApplicationCountByStatusAndHr(hrUserId: Long, status: ApplicationStatus): Int {
        return applicationDao.getApplicationCountByStatusAndHr(hrUserId, status)
    }
    
    /** Đếm số đơn cho một công việc */
    suspend fun getApplicationCountByJob(jobId: Long): Int {
        return applicationDao.getApplicationCountByJob(jobId)
    }
    
    /** Lấy tất cả đơn của HR (cho dashboard) */
    fun getAllApplicationsByHr(hrUserId: Long): LiveData<List<Application>> {
        return applicationDao.getAllApplicationsByHr(hrUserId)
    }
    
    /** Lấy đơn cụ thể của user cho một job */
    suspend fun getApplicationByJobAndUser(jobId: Long, userId: Long): Application? {
        return applicationDao.getApplicationByJobAndApplicant(jobId, userId)
    }
}
