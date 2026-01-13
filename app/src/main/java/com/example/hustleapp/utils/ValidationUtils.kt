package com.example.hustleapp.utils

import android.util.Patterns

/**
 * Utility object for input validation
 */
object ValidationUtils {
    
    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Validate password - minimum 6 characters
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
    
    /**
     * Check if passwords match
     */
    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
    
    /**
     * Validate name - not blank
     */
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
    
    /**
     * Validate job title
     */
    fun isValidJobTitle(title: String): Boolean {
        return title.isNotBlank() && title.length >= 3
    }
    
    /**
     * Validate salary format
     */
    fun isValidSalary(salary: String): Boolean {
        return salary.isNotBlank()
    }
    
    /**
     * Validate location
     */
    fun isValidLocation(location: String): Boolean {
        return location.isNotBlank()
    }
    
    /**
     * Validate description - minimum length
     */
    fun isValidDescription(description: String, minLength: Int = 10): Boolean {
        return description.isNotBlank() && description.length >= minLength
    }
    
    /**
     * Validate post content
     */
    fun isValidPostContent(content: String): Boolean {
        return content.isNotBlank() && content.length >= 3
    }
    
    /**
     * Validate comment content
     */
    fun isValidComment(content: String): Boolean {
        return content.isNotBlank()
    }
    
    /**
     * Validation result wrapper
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )
    
    /**
     * Validate login form
     */
    fun validateLoginForm(email: String, password: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Vui lòng nhập email")
            !isValidEmail(email) -> ValidationResult(false, "Email không hợp lệ")
            password.isBlank() -> ValidationResult(false, "Vui lòng nhập mật khẩu")
            !isValidPassword(password) -> ValidationResult(false, "Mật khẩu phải có ít nhất 6 ký tự")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Validate registration form
     */
    fun validateRegisterForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "Vui lòng nhập họ tên")
            !isValidName(name) -> ValidationResult(false, "Họ tên phải có ít nhất 2 ký tự")
            email.isBlank() -> ValidationResult(false, "Vui lòng nhập email")
            !isValidEmail(email) -> ValidationResult(false, "Email không hợp lệ")
            password.isBlank() -> ValidationResult(false, "Vui lòng nhập mật khẩu")
            !isValidPassword(password) -> ValidationResult(false, "Mật khẩu phải có ít nhất 6 ký tự")
            confirmPassword.isBlank() -> ValidationResult(false, "Vui lòng xác nhận mật khẩu")
            !doPasswordsMatch(password, confirmPassword) -> ValidationResult(false, "Mật khẩu không khớp")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Validate job form
     */
    fun validateJobForm(
        title: String,
        company: String,
        salary: String,
        location: String,
        description: String
    ): ValidationResult {
        return when {
            !isValidJobTitle(title) -> ValidationResult(false, "Tiêu đề công việc không hợp lệ")
            company.isBlank() -> ValidationResult(false, "Vui lòng nhập tên công ty")
            !isValidSalary(salary) -> ValidationResult(false, "Vui lòng nhập mức lương")
            !isValidLocation(location) -> ValidationResult(false, "Vui lòng nhập địa điểm")
            !isValidDescription(description) -> ValidationResult(false, "Mô tả công việc quá ngắn")
            else -> ValidationResult(true)
        }
    }
}
