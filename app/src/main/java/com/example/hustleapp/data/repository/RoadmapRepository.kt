package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.RoadmapDao
import com.example.hustleapp.data.local.entity.RoadmapStep

/**
 * Repository cho tính năng Roadmap (Lộ trình phát triển nghề nghiệp)
 * 
 * Chức năng chính:
 * - Lưu trữ và quản lý các bước lộ trình
 * - Theo dõi tiến độ hoàn thành
 * - Tích hợp với AI Gemini để generate roadmap
 */
class RoadmapRepository(
    private val roadmapDao: RoadmapDao  // DAO cho bảng roadmap_steps
) {
    // ==================== TRUY VẤN ROADMAP ====================
    
    /** Lấy tất cả roadmap của user */
    fun getRoadmapByUserId(userId: Long): LiveData<List<RoadmapStep>> {
        return roadmapDao.getRoadmapByUserId(userId)
    }
    
    /** Lấy roadmap cho một mục tiêu cụ thể (VD: "Senior Developer") */
    fun getRoadmapByUserAndRole(userId: Long, targetRole: String): LiveData<List<RoadmapStep>> {
        return roadmapDao.getRoadmapByUserAndRole(userId, targetRole)
    }
    
    /** Lấy danh sách các mục tiêu mà user đã tạo roadmap */
    fun getTargetRolesByUserId(userId: Long): LiveData<List<String>> {
        return roadmapDao.getTargetRolesByUserId(userId)
    }
    
    // ==================== THAO TÁC ROADMAP ====================
    
    /** Thêm một bước roadmap mới */
    suspend fun addStep(step: RoadmapStep): Long {
        return roadmapDao.insert(step)
    }
    
    /** Cập nhật bước roadmap */
    suspend fun updateStep(step: RoadmapStep) {
        roadmapDao.update(step)
    }
    
    /** Xóa bước roadmap */
    suspend fun deleteStep(step: RoadmapStep) {
        roadmapDao.delete(step)
    }
    
    /** Đánh dấu hoàn thành/chưa hoàn thành một bước */
    suspend fun toggleStepCompletion(stepId: Long, isCompleted: Boolean) {
        roadmapDao.updateStepCompletion(stepId, isCompleted)
    }
    
    // ==================== THỐNG KÊ TIẾN ĐỘ ====================
    
    /**
     * Tính tiến độ hoàn thành (0.0 - 1.0)
     * @return Tỷ lệ số bước đã hoàn thành / tổng số bước
     */
    suspend fun getProgress(userId: Long, targetRole: String): Float {
        val total = roadmapDao.getStepCount(userId, targetRole)
        if (total == 0) return 0f
        val completed = roadmapDao.getCompletedStepCount(userId, targetRole)
        return completed.toFloat() / total.toFloat()
    }
    
    /** Đếm tổng số bước trong roadmap */
    suspend fun getStepCount(userId: Long, targetRole: String): Int {
        return roadmapDao.getStepCount(userId, targetRole)
    }
    
    /** Xóa toàn bộ roadmap để generate lại */
    suspend fun deleteRoadmap(userId: Long, targetRole: String) {
        roadmapDao.deleteRoadmapByUserAndRole(userId, targetRole)
    }
}
