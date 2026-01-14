package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Experience

/**
 * DAO cho bảng Experiences (Kinh nghiệm làm việc)
 * 
 * Quản lý lịch sử công việc của người dùng (Applicant)
 */
@Dao
interface ExperienceDao {
    
    /** Thêm kinh nghiệm mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long
    
    /** Cập nhật thông tin kinh nghiệm */
    @Update
    suspend fun update(experience: Experience)
    
    /** Xóa kinh nghiệm */
    @Delete
    suspend fun delete(experience: Experience)
    
    /** Xóa tất cả kinh nghiệm của user */
    @Query("DELETE FROM experiences WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    /** Lấy danh sách kinh nghiệm (LiveData - sắp xếp mới nhất) */
    @Query("SELECT * FROM experiences WHERE userId = :userId ORDER BY startDate DESC")
    fun getExperiencesByUserId(userId: Long): LiveData<List<Experience>>
    
    /** Lấy danh sách kinh nghiệm (1 lần, không LiveData) */
    @Query("SELECT * FROM experiences WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getExperiencesByUserIdSync(userId: Long): List<Experience>
    
    /** Lấy kinh nghiệm theo ID */
    @Query("SELECT * FROM experiences WHERE id = :id")
    suspend fun getExperienceById(id: Long): Experience?
}
