package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.User
import com.example.hustleapp.data.local.entity.UserRole

/**
 * DAO (Data Access Object) cho bảng Users
 * 
 * DAO là interface định nghĩa các phương thức truy cập database.
 * Room sẽ tự động generate implementation dựa trên các annotation.
 * 
 * Các kiểu trả về:
 * - suspend fun: Hàm bất đồng bộ, phải gọi trong coroutine
 * - LiveData: Tự động cập nhật UI khi dữ liệu thay đổi
 */
@Dao
interface UserDao {
    
    // ==================== THAO TÁC CRUD CƠ BẢN ====================
    
    /**
     * Thêm user mới vào database
     * @return ID của user vừa thêm
     * OnConflict.REPLACE: Nếu trùng khóa chính thì ghi đè
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long
    
    /** Cập nhật thông tin user */
    @Update
    suspend fun update(user: User)
    
    /** Xóa user khỏi database */
    @Delete
    suspend fun delete(user: User)
    
    // ==================== TRUY VẤN USER ====================
    
    /** Lấy user theo ID (1 lần, không tự động cập nhật) */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?
    
    /** Lấy user theo ID (LiveData - tự động cập nhật khi dữ liệu thay đổi) */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdLiveData(userId: Long): LiveData<User?>
    
    /** Tìm user theo email (dùng khi đăng ký để kiểm tra trùng) */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
    
    // ==================== XÁC THỰC ====================
    
    /** 
     * Đăng nhập: Kiểm tra email và password có khớp không
     * @return User nếu đúng, null nếu sai
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?
    
    /** Lọc user theo vai trò (APPLICANT hoặc HR) */
    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRole(role: UserRole): LiveData<List<User>>
    
    /** Kiểm tra email đã tồn tại chưa (dùng khi đăng ký) */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean
    
    /** Lấy tất cả users (dùng cho admin hoặc debug) */
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>
}
