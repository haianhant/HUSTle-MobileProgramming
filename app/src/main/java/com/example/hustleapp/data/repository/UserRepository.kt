package com.example.hustleapp.data.repository

import androidx.lifecycle.LiveData
import com.example.hustleapp.data.local.dao.*
import com.example.hustleapp.data.local.entity.*

/**
 * Repository cho các thao tác liên quan đến User
 * 
 * Repository Pattern:
 * - Là tầng trung gian giữa ViewModel và Data Source (DAO)
 * - Tập hợp nhiều DAO liên quan vào một lớp duy nhất
 * - ViewModel không cần biết dữ liệu lấy từ đâu (local/remote)
 * 
 * Chức năng chính:
 * - Xác thực người dùng (login, register)
 * - Quản lý thông tin profile (user, skills, experience, education)
 */
class UserRepository(
    private val userDao: UserDao,           // DAO cho bảng users
    private val skillDao: SkillDao,         // DAO cho bảng skills
    private val experienceDao: ExperienceDao, // DAO cho bảng experiences
    private val educationDao: EducationDao  // DAO cho bảng education
) {
    // ==================== XÁC THỰC NGƯỜI DÙNG ====================
    
    /**
     * Đăng nhập: Kiểm tra email và password
     * @return User nếu đúng, null nếu sai
     */
    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }
    
    /**
     * Đăng ký tài khoản mới
     * @return ID của user mới
     */
    suspend fun register(user: User): Long {
        return userDao.insert(user)
    }
    
    /** Kiểm tra email đã tồn tại chưa (dùng khi đăng ký) */
    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }
    
    // ==================== THAO TÁC USER ====================
    
    /** Lấy user theo ID (1 lần) */
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    /** Lấy user theo ID (LiveData - tự động cập nhật UI) */
    fun getUserByIdLiveData(userId: Long): LiveData<User?> {
        return userDao.getUserByIdLiveData(userId)
    }
    
    /** Cập nhật thông tin user */
    suspend fun updateUser(user: User) {
        userDao.update(user)
    }
    
    // ==================== THAO TÁC KỸ NĂNG ====================
    
    /** Lấy danh sách kỹ năng của user */
    fun getSkillsByUserId(userId: Long): LiveData<List<Skill>> {
        return skillDao.getSkillsByUserId(userId)
    }
    
    /** Thêm một kỹ năng mới */
    suspend fun addSkill(skill: Skill): Long {
        return skillDao.insert(skill)
    }
    
    /** Xóa một kỹ năng */
    suspend fun deleteSkill(skill: Skill) {
        skillDao.delete(skill)
    }
    
    /** Cập nhật toàn bộ kỹ năng (xóa cũ, thêm mới) */
    suspend fun updateSkills(userId: Long, skills: List<Skill>) {
        skillDao.deleteAllByUserId(userId)
        skillDao.insertAll(skills)
    }
    
    // ==================== THAO TÁC KINH NGHIỆM ====================
    
    /** Lấy danh sách kinh nghiệm */
    fun getExperiencesByUserId(userId: Long): LiveData<List<Experience>> {
        return experienceDao.getExperiencesByUserId(userId)
    }
    
    /** Thêm kinh nghiệm mới */
    suspend fun addExperience(experience: Experience): Long {
        return experienceDao.insert(experience)
    }
    
    /** Cập nhật kinh nghiệm */
    suspend fun updateExperience(experience: Experience) {
        experienceDao.update(experience)
    }
    
    /** Xóa kinh nghiệm */
    suspend fun deleteExperience(experience: Experience) {
        experienceDao.delete(experience)
    }
    
    // ==================== THAO TÁC HỌC VẤN ====================
    
    /** Lấy danh sách học vấn */
    fun getEducationByUserId(userId: Long): LiveData<List<Education>> {
        return educationDao.getEducationByUserId(userId)
    }
    
    /** Thêm học vấn mới */
    suspend fun addEducation(education: Education): Long {
        return educationDao.insert(education)
    }
    
    /** Cập nhật học vấn */
    suspend fun updateEducation(education: Education) {
        educationDao.update(education)
    }
    
    /** Xóa học vấn */
    suspend fun deleteEducation(education: Education) {
        educationDao.delete(education)
    }
}
