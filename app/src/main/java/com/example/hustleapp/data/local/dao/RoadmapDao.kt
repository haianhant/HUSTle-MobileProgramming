package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.RoadmapStep

@Dao
interface RoadmapDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roadmapStep: RoadmapStep): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(steps: List<RoadmapStep>)
    
    @Update
    suspend fun update(roadmapStep: RoadmapStep)
    
    @Delete
    suspend fun delete(roadmapStep: RoadmapStep)
    
    @Query("SELECT * FROM roadmap_steps WHERE id = :stepId")
    suspend fun getStepById(stepId: Long): RoadmapStep?
    
    @Query("SELECT * FROM roadmap_steps WHERE userId = :userId ORDER BY stepNumber ASC")
    fun getRoadmapByUserId(userId: Long): LiveData<List<RoadmapStep>>
    
    @Query("SELECT * FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole ORDER BY stepNumber ASC")
    fun getRoadmapByUserAndRole(userId: Long, targetRole: String): LiveData<List<RoadmapStep>>
    
    @Query("UPDATE roadmap_steps SET isCompleted = :isCompleted WHERE id = :stepId")
    suspend fun updateStepCompletion(stepId: Long, isCompleted: Boolean)
    
    @Query("SELECT DISTINCT targetRole FROM roadmap_steps WHERE userId = :userId")
    fun getTargetRolesByUserId(userId: Long): LiveData<List<String>>
    
    @Query("SELECT COUNT(*) FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole")
    suspend fun getStepCount(userId: Long, targetRole: String): Int
    
    @Query("SELECT COUNT(*) FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole AND isCompleted = 1")
    suspend fun getCompletedStepCount(userId: Long, targetRole: String): Int
    
    @Query("DELETE FROM roadmap_steps WHERE userId = :userId AND targetRole = :targetRole")
    suspend fun deleteRoadmapByUserAndRole(userId: Long, targetRole: String)
}
