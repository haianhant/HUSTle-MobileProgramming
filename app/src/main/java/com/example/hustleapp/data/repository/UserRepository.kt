package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.*
import com.example.hustleapp.data.local.entity.*

/**
 * Repository for user-related data operations
 */
class UserRepository(
    private val userDao: UserDao,
    private val skillDao: SkillDao,
    private val experienceDao: ExperienceDao,
    private val educationDao: EducationDao
) {
    // User operations
    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }
    
    suspend fun register(user: User): Long {
        return userDao.insert(user)
    }
    
    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }
    
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    fun getUserByIdLiveData(userId: Long): LiveData<User?> {
        return userDao.getUserByIdLiveData(userId)
    }
    
    suspend fun updateUser(user: User) {
        userDao.update(user)
    }
    
    // Skills operations
    fun getSkillsByUserId(userId: Long): LiveData<List<Skill>> {
        return skillDao.getSkillsByUserId(userId)
    }
    
    suspend fun addSkill(skill: Skill): Long {
        return skillDao.insert(skill)
    }
    
    suspend fun deleteSkill(skill: Skill) {
        skillDao.delete(skill)
    }
    
    suspend fun updateSkills(userId: Long, skills: List<Skill>) {
        skillDao.deleteAllByUserId(userId)
        skillDao.insertAll(skills)
    }
    
    // Experience operations
    fun getExperiencesByUserId(userId: Long): LiveData<List<Experience>> {
        return experienceDao.getExperiencesByUserId(userId)
    }
    
    suspend fun addExperience(experience: Experience): Long {
        return experienceDao.insert(experience)
    }
    
    suspend fun updateExperience(experience: Experience) {
        experienceDao.update(experience)
    }
    
    suspend fun deleteExperience(experience: Experience) {
        experienceDao.delete(experience)
    }
    
    // Education operations
    fun getEducationByUserId(userId: Long): LiveData<List<Education>> {
        return educationDao.getEducationByUserId(userId)
    }
    
    suspend fun addEducation(education: Education): Long {
        return educationDao.insert(education)
    }
    
    suspend fun updateEducation(education: Education) {
        educationDao.update(education)
    }
    
    suspend fun deleteEducation(education: Education) {
        educationDao.delete(education)
    }
}
