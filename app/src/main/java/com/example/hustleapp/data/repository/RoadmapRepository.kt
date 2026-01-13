package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.RoadmapDao
import com.example.hustleapp.data.local.entity.RoadmapStep

/**
 * Repository for roadmap/career development data operations
 */
class RoadmapRepository(
    private val roadmapDao: RoadmapDao
) {
    fun getRoadmapByUserId(userId: Long): LiveData<List<RoadmapStep>> {
        return roadmapDao.getRoadmapByUserId(userId)
    }
    
    fun getRoadmapByUserAndRole(userId: Long, targetRole: String): LiveData<List<RoadmapStep>> {
        return roadmapDao.getRoadmapByUserAndRole(userId, targetRole)
    }
    
    fun getTargetRolesByUserId(userId: Long): LiveData<List<String>> {
        return roadmapDao.getTargetRolesByUserId(userId)
    }
    
    suspend fun addStep(step: RoadmapStep): Long {
        return roadmapDao.insert(step)
    }
    
    suspend fun updateStep(step: RoadmapStep) {
        roadmapDao.update(step)
    }
    
    suspend fun deleteStep(step: RoadmapStep) {
        roadmapDao.delete(step)
    }
    
    suspend fun toggleStepCompletion(stepId: Long, isCompleted: Boolean) {
        roadmapDao.updateStepCompletion(stepId, isCompleted)
    }
    
    suspend fun getProgress(userId: Long, targetRole: String): Float {
        val total = roadmapDao.getStepCount(userId, targetRole)
        if (total == 0) return 0f
        val completed = roadmapDao.getCompletedStepCount(userId, targetRole)
        return completed.toFloat() / total.toFloat()
    }
    
    suspend fun getStepCount(userId: Long, targetRole: String): Int {
        return roadmapDao.getStepCount(userId, targetRole)
    }
    
    suspend fun deleteRoadmap(userId: Long, targetRole: String) {
        roadmapDao.deleteRoadmapByUserAndRole(userId, targetRole)
    }
}
