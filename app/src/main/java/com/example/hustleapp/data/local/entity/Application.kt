package com.example.hustleapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Enum trạng thái đơn ứng tuyển
 * 
 * PENDING: Đang chờ xem xét - đơn mới gửi, HR chưa xử lý
 * SHORTLISTED: Đã vào danh sách ngắn - ứng viên tiềm năng
 * REJECTED: Đã từ chối - ứng viên không phù hợp
 */
enum class ApplicationStatus {
    PENDING,     // Đang chờ xử lý
    SHORTLISTED, // Vào danh sách ngắn (ứng viên tiềm năng)
    REJECTED     // Đã từ chối
}

/**
 * Entity Application - Đại diện cho bảng "applications" trong Room Database
 * Lưu trữ thông tin các đơn ứng tuyển từ Applicant cho các Job
 * 
 * Mối quan hệ:
 * - Một Job có thể có nhiều Application (1-N)
 * - Một User (Applicant) có thể có nhiều Application (1-N)
 * - Application là bảng trung gian thể hiện mối quan hệ N-N giữa User và Job
 * 
 * @ForeignKey với Job: Liên kết đơn ứng tuyển với tin tuyển dụng
 * @ForeignKey với User: Liên kết đơn ứng tuyển với người nộp đơn
 */
@Entity(
    tableName = "applications",
    foreignKeys = [
        // Khóa ngoại liên kết với bảng jobs
        ForeignKey(
            entity = Job::class,
            parentColumns = ["id"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE // Xóa Job sẽ xóa tất cả đơn ứng tuyển
        ),
        // Khóa ngoại liên kết với bảng users (applicant)
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["applicantId"],
            onDelete = ForeignKey.CASCADE // Xóa User sẽ xóa tất cả đơn của user đó
        )
    ],
    // Index để tăng tốc truy vấn theo jobId và applicantId
    indices = [Index("jobId"), Index("applicantId")]
)
data class Application(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                       // ID đơn ứng tuyển (tự động tăng)
    val jobId: Long,                        // ID công việc ứng tuyển (khóa ngoại)
    val applicantId: Long,                  // ID người nộp đơn (khóa ngoại)
    val status: ApplicationStatus = ApplicationStatus.PENDING, // Trạng thái đơn (mặc định: PENDING)
    val cvUrl: String? = null,              // URL đến CV đính kèm (tùy chọn)
    val coverLetter: String? = null,        // Thư xin việc (tùy chọn)
    val appliedAt: Long = System.currentTimeMillis() // Thời gian nộp đơn
)
