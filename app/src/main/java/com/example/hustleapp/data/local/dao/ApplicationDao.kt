package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus

/**
 * DAO cho bảng Applications (Đơn ứng tuyển)
 * 
 * Cung cấp các phương thức:
 * - CRUD cho đơn ứng tuyển
 * - Truy vấn đơn theo công việc, ứng viên
 * - Thống kê cho HR (tổng đơn, theo trạng thái...)
 */
@Dao
interface ApplicationDao {
    
    // ==================== THAO TÁC CRUD CƠ BẢN ====================
    
    /** Thêm đơn ứng tuyển mới, trả về ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: Application): Long
    
    /** Cập nhật đơn ứng tuyển */
    @Update
    suspend fun update(application: Application)
    
    /** Xóa đơn ứng tuyển */
    @Delete
    suspend fun delete(application: Application)
    
    // ==================== TRUY VẤN ĐƠN ỨNG TUYỂN ====================
    
    /** Lấy đơn theo ID */
    @Query("SELECT * FROM applications WHERE id = :applicationId")
    suspend fun getApplicationById(applicationId: Long): Application?
    
    /** Lấy tất cả đơn cho một công việc (HR xem danh sách ứng viên) */
    @Query("SELECT * FROM applications WHERE jobId = :jobId ORDER BY appliedAt DESC")
    fun getApplicationsByJobId(jobId: Long): LiveData<List<Application>>
    
    /** Lấy tất cả đơn của một ứng viên (Applicant xem lịch sử ứng tuyển) */
    @Query("SELECT * FROM applications WHERE applicantId = :applicantId ORDER BY appliedAt DESC")
    fun getApplicationsByApplicantId(applicantId: Long): LiveData<List<Application>>
    
    /** Lấy đơn cụ thể của ứng viên cho một công việc */
    @Query("SELECT * FROM applications WHERE jobId = :jobId AND applicantId = :applicantId LIMIT 1")
    suspend fun getApplicationByJobAndApplicant(jobId: Long, applicantId: Long): Application?
    
    /** Kiểm tra ứng viên đã ứng tuyển công việc này chưa (ngăn nộp đơn trùng) */
    @Query("SELECT EXISTS(SELECT 1 FROM applications WHERE jobId = :jobId AND applicantId = :applicantId)")
    suspend fun hasApplied(jobId: Long, applicantId: Long): Boolean
    
    // ==================== CẬP NHẬT TRẠNG THÁI ====================
    
    /** HR cập nhật trạng thái đơn (PENDING -> SHORTLISTED/REJECTED) */
    @Query("UPDATE applications SET status = :status WHERE id = :applicationId")
    suspend fun updateStatus(applicationId: Long, status: ApplicationStatus)
    
    // ==================== THỐNG KÊ CHO HR ====================
    
    /** Tổng số đơn ứng tuyển cho tất cả tin của một HR */
    @Query("SELECT COUNT(*) FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId)")
    suspend fun getTotalApplicationsByHr(hrUserId: Long): Int
    
    /** Đếm đơn theo trạng thái cho một HR (VD: bao nhiêu đơn SHORTLISTED) */
    @Query("SELECT COUNT(*) FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId) AND status = :status")
    suspend fun getApplicationCountByStatusAndHr(hrUserId: Long, status: ApplicationStatus): Int
    
    /** Đếm số đơn cho một công việc cụ thể */
    @Query("SELECT COUNT(*) FROM applications WHERE jobId = :jobId")
    suspend fun getApplicationCountByJob(jobId: Long): Int
    
    /** Đếm đơn theo trạng thái cho một công việc */
    @Query("SELECT COUNT(*) FROM applications WHERE jobId = :jobId AND status = :status")
    suspend fun getApplicationCountByJobAndStatus(jobId: Long, status: ApplicationStatus): Int
    
    /** Lấy tất cả đơn của một HR (hiển thị trong dashboard HR) */
    @Query("SELECT * FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId) ORDER BY appliedAt DESC")
    fun getAllApplicationsByHr(hrUserId: Long): LiveData<List<Application>>
}
