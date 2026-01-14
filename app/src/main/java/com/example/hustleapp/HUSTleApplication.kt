package com.example.hustleapp

import android.app.Application
import com.example.hustleapp.data.local.database.HUSTleDatabase
import com.example.hustleapp.data.repository.JobRepository
import com.example.hustleapp.data.repository.PostRepository
import com.example.hustleapp.data.repository.RoadmapRepository
import com.example.hustleapp.data.repository.UserRepository
import com.example.hustleapp.utils.SessionManager

/**
 * Lớp Application - Điểm khởi đầu của ứng dụng Android
 * 
 * Lớp này kế thừa từ Application và được khởi tạo trước tất cả các Activity.
 * Mục đích chính:
 * - Khởi tạo và cung cấp các dependency toàn ứng dụng (database, repositories)
 * - Quản lý vòng đời ứng dụng
 * - Cung cấp context toàn cục thông qua singleton pattern
 */
class HUSTleApplication : Application() {
    
    // ==================== KHỞI TẠO DATABASE ====================
    // Sử dụng lazy để database chỉ được khởi tạo khi cần thiết (lazy initialization)
    // Điều này giúp tối ưu hiệu suất khởi động ứng dụng
    val database by lazy { HUSTleDatabase.getDatabase(this) }
    
    // ==================== REPOSITORIES ====================
    // Repository pattern: Tầng trung gian giữa ViewModel và Data Source (DAO)
    // Mỗi repository quản lý một nhóm dữ liệu liên quan
    
    // Repository quản lý thông tin người dùng, kỹ năng, kinh nghiệm và học vấn
    val userRepository by lazy {
        UserRepository(
            database.userDao(),      // DAO cho bảng users
            database.skillDao(),     // DAO cho bảng skills  
            database.experienceDao(),// DAO cho bảng experiences
            database.educationDao()  // DAO cho bảng education
        )
    }
    
    // Repository quản lý tin tuyển dụng và đơn ứng tuyển
    val jobRepository by lazy {
        JobRepository(
            database.jobDao(),        // DAO cho bảng jobs
            database.applicationDao() // DAO cho bảng applications
        )
    }
    
    // Repository quản lý bài đăng cộng đồng và bình luận
    val postRepository by lazy {
        PostRepository(
            database.postDao(),    // DAO cho bảng posts
            database.commentDao()  // DAO cho bảng comments
        )
    }
    
    // Repository quản lý lộ trình phát triển nghề nghiệp
    val roadmapRepository by lazy {
        RoadmapRepository(
            database.roadmapDao()  // DAO cho bảng roadmap_steps
        )
    }
    
    // ==================== SESSION MANAGER ====================
    // Quản lý phiên đăng nhập của người dùng sử dụng SharedPreferences
    // Lưu trữ thông tin như: userId, userRole, trạng thái đăng nhập
    val sessionManager by lazy { SessionManager(this) }
    
    /**
     * Được gọi khi ứng dụng khởi động
     * Đây là nơi đầu tiên code chạy khi app được mở
     */
    override fun onCreate() {
        super.onCreate()
        // Lưu instance để có thể truy cập từ bất kỳ đâu trong ứng dụng
        instance = this
    }
    
    // ==================== SINGLETON PATTERN ====================
    companion object {
        // Instance tĩnh cho phép truy cập Application từ bất kỳ đâu
        // Ví dụ: HUSTleApplication.instance.userRepository
        lateinit var instance: HUSTleApplication
            private set // Chỉ cho phép set từ bên trong lớp này
    }
}
