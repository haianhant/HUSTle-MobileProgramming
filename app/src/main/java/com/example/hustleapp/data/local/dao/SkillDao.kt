package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Skill

/**
 * DAO cho bảng Skills (Kỹ năng của người dùng)
 * 
 * Quản lý các kỹ năng chuyên môn như: Java, Kotlin, Android, UI/UX...
 * Một user có thể có nhiều skills
 */
@Dao
interface SkillDao {
    
    /** Thêm một kỹ năng mới */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(skill: Skill): Long
    
    /** Thêm nhiều kỹ năng cùng lúc (dùng khi cập nhật profile) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skills: List<Skill>)
    
    /** Cập nhật tên kỹ năng */
    @Update
    suspend fun update(skill: Skill)
    
    /** Xóa một kỹ năng */
    @Delete
    suspend fun delete(skill: Skill)
    
    /** Xóa tất cả kỹ năng của user (trước khi cập nhật lại danh sách mới) */
    @Query("DELETE FROM skills WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    /** Lấy danh sách kỹ năng theo userId (LiveData - tự cập nhật UI) */
    @Query("SELECT * FROM skills WHERE userId = :userId")
    fun getSkillsByUserId(userId: Long): LiveData<List<Skill>>
    
    /** Lấy danh sách kỹ năng (1 lần, không LiveData) */
    @Query("SELECT * FROM skills WHERE userId = :userId")
    suspend fun getSkillsByUserIdSync(userId: Long): List<Skill>
}
