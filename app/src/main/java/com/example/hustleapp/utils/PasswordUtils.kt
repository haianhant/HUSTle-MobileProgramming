package com.example.hustleapp.utils

import java.security.MessageDigest

/**
 * Utility class cho việc băm mật khẩu sử dụng SHA-256
 * 
 * LƯU Ý: File này hiện tại KHÔNG ĐƯỢC SỬ DỤNG trong app.
 * Đây chỉ là code tham khảo để demo cách băm mật khẩu.
 * 
 * SHA-256 (Secure Hash Algorithm 256-bit):
 * - Tạo ra chuỗi hash 64 ký tự hex (256 bit)
 * - Một chiều: không thể giải mã ngược từ hash ra password gốc
 * - Deterministic: cùng input luôn cho cùng output
 * 
 * Cách hoạt động:
 * - Password "123456" → SHA-256 → "8d969eef6ecad3c29a3a629280e686cf..."
 * - Khi login, hash password nhập vào và so sánh với hash đã lưu
 */
object PasswordUtils {
    
    /**
     * Băm mật khẩu sử dụng thuật toán SHA-256
     * 
     * @param password Mật khẩu gốc (plain text)
     * @return Chuỗi hash 64 ký tự hex
     * 
     * Ví dụ:
     *   hashPassword("123456") → "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
     *   hashPassword("admin")  → "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"
     */
    fun hashPassword(password: String): String {
        // Tạo instance của MessageDigest với thuật toán SHA-256
        val messageDigest = MessageDigest.getInstance("SHA-256")
        
        // Chuyển password thành mảng byte (UTF-8 encoding)
        val passwordBytes = password.toByteArray(Charsets.UTF_8)
        
        // Thực hiện băm và nhận kết quả dạng mảng byte
        val hashBytes = messageDigest.digest(passwordBytes)
        
        // Chuyển mảng byte thành chuỗi hex
        return hashBytes.joinToString("") { byte ->
            // Chuyển mỗi byte thành 2 ký tự hex (vd: 0x8d → "8d")
            "%02x".format(byte)
        }
    }
    
    /**
     * Xác thực mật khẩu bằng cách so sánh hash
     * 
     * @param inputPassword Mật khẩu người dùng nhập vào
     * @param storedHash Hash đã lưu trong database
     * @return true nếu mật khẩu đúng, false nếu sai
     * 
     * Ví dụ:
     *   verifyPassword("123456", "8d969eef6ecad3c29a3a629280e686cf...") → true
     *   verifyPassword("wrong",  "8d969eef6ecad3c29a3a629280e686cf...") → false
     */
    fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        // Hash password nhập vào
        val inputHash = hashPassword(inputPassword)
        
        // So sánh 2 hash (case-insensitive để tránh lỗi hex uppercase/lowercase)
        return inputHash.equals(storedHash, ignoreCase = true)
    }
    
    // ==================== DEMO / TEST ====================
    
    /**
     * Hàm demo để test (có thể gọi từ bất kỳ đâu để kiểm tra)
     * 
     * Ví dụ sử dụng:
     *   PasswordUtils.demo()
     * 
     * Output:
     *   Password: 123456
     *   SHA-256 Hash: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
     *   Verify '123456': true
     *   Verify 'wrong': false
     */
    fun demo() {
        val testPassword = "123456"
        val hash = hashPassword(testPassword)
        
        println("==================== PASSWORD HASHING DEMO ====================")
        println("Password: $testPassword")
        println("SHA-256 Hash: $hash")
        println("Hash length: ${hash.length} characters")
        println()
        println("Verification test:")
        println("  Verify '$testPassword': ${verifyPassword(testPassword, hash)}")
        println("  Verify 'wrong': ${verifyPassword("wrong", hash)}")
        println("================================================================")
    }
}

/*
 * ==================== HƯỚNG DẪN TÍCH HỢP (KHÔNG APPLY) ====================
 * 
 * Nếu muốn tích hợp vào app sau này, cần sửa:
 * 
 * 1. RegisterViewModel.kt - Khi đăng ký:
 *    val hashedPassword = PasswordUtils.hashPassword(passwordValue)
 *    val user = User(
 *        email = emailValue,
 *        password = hashedPassword,  // Lưu hash thay vì plain text
 *        ...
 *    )
 * 
 * 2. UserDao.kt - Thay đổi query login:
 *    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
 *    suspend fun getUserByEmail(email: String): User?
 *    
 * 3. UserRepository.kt - Thêm logic verify:
 *    suspend fun login(email: String, password: String): User? {
 *        val user = userDao.getUserByEmail(email) ?: return null
 *        return if (PasswordUtils.verifyPassword(password, user.password)) {
 *            user
 *        } else {
 *            null
 *        }
 *    }
 * 
 * 4. HUSTleDatabase.kt - Hash password trong sample data:
 *    val hrUser = User(
 *        password = PasswordUtils.hashPassword("123456"),
 *        ...
 *    )
 * 
 * LƯU Ý: Sau khi apply, phải uninstall app để database tạo lại với hash mới!
 */
