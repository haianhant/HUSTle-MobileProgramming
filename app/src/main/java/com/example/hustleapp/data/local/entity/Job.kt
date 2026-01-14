package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Enum trạng thái của tin tuyển dụng
 * 
 * OPEN: Tin đang mở, ứng viên có thể ứng tuyển
 * CLOSED: Tin đã đóng, không nhận thêm đơn ứng tuyển
 */
enum class JobStatus {
    OPEN,    // Đang tuyển dụng
    CLOSED   // Đã đóng tuyển dụng
}

/**
 * Entity Job - Đại diện cho bảng "jobs" trong Room Database
 * Lưu trữ thông tin các tin tuyển dụng do HR đăng
 * 
 * @ForeignKey: Ràng buộc khóa ngoại liên kết với bảng users
 * - parentColumns: Cột khóa chính của bảng cha (users.id)
 * - childColumns: Cột khóa ngoại của bảng này (hrUserId)
 * - onDelete CASCADE: Khi xóa User thì tất cả Job của user đó cũng bị xóa
 * 
 * @Index: Tạo index trên cột hrUserId để tăng tốc truy vấn
 */
@Entity(
    tableName = "jobs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,      // Bảng cha liên kết
            parentColumns = ["id"],    // Cột khóa chính bảng cha
            childColumns = ["hrUserId"], // Cột khóa ngoại bảng này
            onDelete = ForeignKey.CASCADE // Xóa theo dây chuyền
        )
    ],
    indices = [Index("hrUserId")]  // Index để tăng tốc tìm kiếm theo HR
)
data class Job(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,               // ID tin tuyển dụng (tự động tăng)
    val hrUserId: Long,             // ID của HR đăng tin (khóa ngoại)
    val title: String,              // Tiêu đề công việc
    val company: String,            // Tên công ty
    val salary: String,             // Mức lương
    val location: String,           // Địa điểm làm việc
    val description: String,        // Mô tả công việc chi tiết
    val requirements: String,       // Yêu cầu ứng viên
    val status: JobStatus = JobStatus.OPEN, // Trạng thái tin (mặc định: OPEN)
    val viewCount: Int = 0,         // Số lượt xem tin
    val createdAt: Long = System.currentTimeMillis() // Thời gian đăng tin
)
