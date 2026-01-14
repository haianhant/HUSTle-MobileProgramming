package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.RoadmapStep

/**
 * DAO cho bảng RoadmapSteps (Lộ trình phát triển nghề nghiệp)
 * 
 * Quản lý các bước trong lộ trình do AI (Gemini) tạo ra
 * Mỗi user có thể có nhiều roadmap cho các mục tiêu khác nhau
 */
@Dao
interface RoadmapDao {
    
    // ==================== THAO TÁC CRUD ====================
    
    /** Thêm một bước roadmap mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roadmapStep: RoadmapStep): Long
    
    /** Thêm toàn bộ các bước của roadmap (khi AI generate) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(steps: List<RoadmapStep>)
    
    /** Cập nhật bước roadmap */
    @Update
    suspend fun update(roadmapStep: RoadmapStep)
    
    /** Xóa bước roadmap */
    @Delete
    suspend fun delete(roadmapStep: RoadmapStep)
    
    // ==================== TRUY VẤN ROADMAP ====================
    
    /** Lấy bước theo ID */
    @Query("SELECT * FROM roadmap_steps WHERE id = :stepId")
    suspend fun getStepById(stepId: Long): RoadmapStep?
    
    /** Lấy tất cả roadmap của user (sắp xếp theo thứ tự bước) */
    @Query("SELECT * FROM roadmap_steps WHERE userId = :userId ORDER BY stepNumber ASC")
    fun getRoadmapByUserId(userId: Long): LiveData<List<RoadmapStep>>
    
    /** Lấy roadmap cho một mục tiêu cụ thể (VD: "Senior Developer") */
    @Query("SELECT * FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole ORDER BY stepNumber ASC")
    fun getRoadmapByUserAndRole(userId: Long, targetRole: String): LiveData<List<RoadmapStep>>
    
    // ==================== CẬP NHẬT TIẾN ĐỘ ====================
    
    /** Đánh dấu hoàn thành/chưa hoàn thành một bước */
    @Query("UPDATE roadmap_steps SET isCompleted = :isCompleted WHERE id = :stepId")
    suspend fun updateStepCompletion(stepId: Long, isCompleted: Boolean)
    
    // ==================== THỐNG KÊ ====================
    
    /** Lấy danh sách các mục tiêu khác nhau của user */
    @Query("SELECT DISTINCT targetRole FROM roadmap_steps WHERE userId = :userId")
    fun getTargetRolesByUserId(userId: Long): LiveData<List<String>>
    
    /** Đếm tổng số bước trong roadmap */
    @Query("SELECT COUNT(*) FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole")
    suspend fun getStepCount(userId: Long, targetRole: String): Int
    
    /** Đếm số bước đã hoàn thành (để tính tiến độ %) */
    @Query("SELECT COUNT(*) FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole AND isCompleted = 1")
    suspend fun getCompletedStepCount(userId: Long, targetRole: String): Int
    
    /** Xóa toàn bộ roadmap cho một mục tiêu (khi muốn generate lại) */
    @Query("DELETE FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole")
    suspend fun deleteRoadmapByUserAndRole(userId: Long, targetRole: String)
}
