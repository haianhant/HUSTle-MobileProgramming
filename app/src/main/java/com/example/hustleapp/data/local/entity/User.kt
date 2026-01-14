package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Enum định nghĩa vai trò người dùng trong ứng dụng
 * 
 * APPLICANT: Người tìm việc - có thể xem và ứng tuyển việc làm
 * HR: Nhà tuyển dụng - có thể đăng tin và quản lý ứng viên
 * 
 * Vai trò này quyết định:
 * - Màn hình nào người dùng sẽ thấy sau khi đăng nhập
 * - Các chức năng nào người dùng có thể sử dụng
 */
enum class UserRole {
    APPLICANT,  // Ứng viên tìm việc
    HR          // Nhà tuyển dụng (Human Resources)
}

/**
 * Entity User - Đại diện cho bảng "users" trong Room Database
 * 
 * @Entity: Annotation đánh dấu đây là một bảng trong database
 * @PrimaryKey: Khóa chính của bảng, autoGenerate = true để tự động tăng
 * 
 * Entity này lưu trữ thông tin của cả Applicant và HR
 * Phân biệt bằng trường 'role'
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,           // ID tự động tăng, khóa chính
    val email: String,          // Email đăng nhập (unique)
    val password: String,       // Mật khẩu (đã hash)
    val name: String,           // Tên hiển thị của người dùng
    val role: UserRole,         // Vai trò: APPLICANT hoặc HR
    val avatarUrl: String? = null,    // URL ảnh đại diện (có thể null)
    val headline: String? = null,     // Tiêu đề/chức danh nghề nghiệp
    val about: String? = null,        // Mô tả bản thân
    val createdAt: Long = System.currentTimeMillis() // Thời gian tạo tài khoản (timestamp)
)
