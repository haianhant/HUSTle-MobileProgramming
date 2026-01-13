package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Education

@Dao
interface EducationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(education: Education): Long
    
    @Update
    suspend fun update(education: Education)
    
    @Delete
    suspend fun delete(education: Education)
    
    @Query("DELETE FROM education WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    @Query("SELECT * FROM education WHERE userId = :userId ORDER BY startDate DESC")
    fun getEducationByUserId(userId: Long): LiveData<List<Education>>
    
    @Query("SELECT * FROM education WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getEducationByUserIdSync(userId: Long): List<Education>
    
    @Query("SELECT * FROM education WHERE id = :id")
    suspend fun getEducationById(id: Long): Education?
}
