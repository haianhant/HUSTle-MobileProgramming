package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.data.local.entity.JobStatus

/**
 * DAO cho bảng Jobs (Tin tuyển dụng)
 * 
 * Cung cấp các phương thức:
 * - CRUD cơ bản cho tin tuyển dụng
 * - Lọc, tìm kiếm tin theo nhiều tiêu chí
 * - Thống kê cho HR (số lượt xem, số tin đăng...)
 */
@Dao
interface JobDao {
    
    // ==================== THAO TÁC CRUD CƠ BẢN ====================
    
    /** Thêm tin tuyển dụng mới, trả về ID của tin */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: Job): Long
    
    /** Cập nhật thông tin tin tuyển dụng */
    @Update
    suspend fun update(job: Job)
    
    /** Xóa tin tuyển dụng */
    @Delete
    suspend fun delete(job: Job)
    
    // ==================== TRUY VẤN TIN TUYỂN DỤNG ====================
    
    /** Lấy tin theo ID (trả về 1 lần) */
    @Query("SELECT * FROM jobs WHERE id = :jobId")
    suspend fun getJobById(jobId: Long): Job?
    
    /** Lấy tin theo ID (LiveData - tự động cập nhật UI) */
    @Query("SELECT * FROM jobs WHERE id = :jobId")
    fun getJobByIdLiveData(jobId: Long): LiveData<Job?>
    
    /** Lọc tin theo trạng thái (OPEN/CLOSED), sắp xếp mới nhất trước */
    @Query("SELECT * FROM jobs WHERE status = :status ORDER BY createdAt DESC")
    fun getJobsByStatus(status: JobStatus): LiveData<List<Job>>
    
    /** Lấy tất cả tin (bao gồm cả đã đóng) */
    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobs(): LiveData<List<Job>>
    
    /** Lấy các tin đang mở (cho ứng viên xem) */
    @Query("SELECT * FROM jobs WHERE status = 'OPEN' ORDER BY createdAt DESC")
    fun getOpenJobs(): LiveData<List<Job>>
    
    /** Lấy tin của một HR cụ thể (cho màn hình quản lý tin HR) */
    @Query("SELECT * FROM jobs WHERE hrUserId = :hrUserId ORDER BY createdAt DESC")
    fun getJobsByHrId(hrUserId: Long): LiveData<List<Job>>
    
    /** Tìm kiếm tin theo từ khóa trong tiêu đề, công ty hoặc địa điểm */
    @Query("SELECT * FROM jobs WHERE title LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchJobs(query: String): LiveData<List<Job>>
    
    // ==================== CẬP NHẬT TRẠNG THÁI ====================
    
    /** Tăng lượt xem của tin (mỗi lần ứng viên xem chi tiết tin) */
    @Query("UPDATE jobs SET viewCount = viewCount + 1 WHERE id = :jobId")
    suspend fun incrementViewCount(jobId: Long)
    
    /** Cập nhật trạng thái tin (OPEN -> CLOSED hoặc ngược lại) */
    @Query("UPDATE jobs SET status = :status WHERE id = :jobId")
    suspend fun updateJobStatus(jobId: Long, status: JobStatus)
    
    // ==================== THỐNG KÊ CHO HR ====================
    
    /** Đếm số tin của một HR */
    @Query("SELECT COUNT(*) FROM jobs WHERE hrUserId = :hrUserId")
    suspend fun getJobCountByHr(hrUserId: Long): Int
    
    /** Tổng số lượt xem tất cả tin của một HR */
    @Query("SELECT SUM(viewCount) FROM jobs WHERE hrUserId = :hrUserId")
    suspend fun getTotalViewsByHr(hrUserId: Long): Int?
}
