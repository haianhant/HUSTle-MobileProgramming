package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Education

/**
 * DAO cho bảng Education (Học vấn)
 * 
 * Quản lý thông tin học vấn của người dùng (Applicant)
 */
@Dao
interface EducationDao {
    
    /** Thêm thông tin học vấn mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(education: Education): Long
    
    /** Cập nhật thông tin học vấn */
    @Update
    suspend fun update(education: Education)
    
    /** Xóa thông tin học vấn */
    @Delete
    suspend fun delete(education: Education)
    
    /** Xóa tất cả học vấn của user */
    @Query("DELETE FROM education WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    /** Lấy danh sách học vấn (LiveData - sắp xếp mới nhất) */
    @Query("SELECT * FROM education WHERE userId = :userId ORDER BY startDate DESC")
    fun getEducationByUserId(userId: Long): LiveData<List<Education>>
    
    /** Lấy danh sách học vấn (1 lần, không LiveData) */
    @Query("SELECT * FROM education WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getEducationByUserIdSync(userId: Long): List<Education>
    
    /** Lấy học vấn theo ID */
    @Query("SELECT * FROM education WHERE id = :id")
    suspend fun getEducationById(id: Long): Education?
}
