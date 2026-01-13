package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Experience

@Dao
interface ExperienceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long
    
    @Update
    suspend fun update(experience: Experience)
    
    @Delete
    suspend fun delete(experience: Experience)
    
    @Query("DELETE FROM experiences WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    @Query("SELECT * FROM experiences WHERE userId = :userId ORDER BY startDate DESC")
    fun getExperiencesByUserId(userId: Long): LiveData<List<Experience>>
    
    @Query("SELECT * FROM experiences WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getExperiencesByUserIdSync(userId: Long): List<Experience>
    
    @Query("SELECT * FROM experiences WHERE id = :id")
    suspend fun getExperienceById(id: Long): Experience?
}
