package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Skill

@Dao
interface SkillDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(skill: Skill): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skills: List<Skill>)
    
    @Update
    suspend fun update(skill: Skill)
    
    @Delete
    suspend fun delete(skill: Skill)
    
    @Query("DELETE FROM skills WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    @Query("SELECT * FROM skills WHERE userId = :userId")
    fun getSkillsByUserId(userId: Long): LiveData<List<Skill>>
    
    @Query("SELECT * FROM skills WHERE userId = :userId")
    suspend fun getSkillsByUserIdSync(userId: Long): List<Skill>
}
