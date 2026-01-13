package com.example.hustleapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.hustleapp.data.local.entity.UserRole

/**
 * Session manager for handling user login state
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "hustle_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Save user session after successful login
     */
    fun saveSession(userId: Long, email: String, name: String, role: UserRole) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_ROLE, role.name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Clear user session on logout
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Get current user ID
     */
    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1)
    }
    
    /**
     * Get current user email
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Get current user name
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Get current user role
     */
    fun getUserRole(): UserRole? {
        val roleString = prefs.getString(KEY_USER_ROLE, null)
        return roleString?.let { UserRole.valueOf(it) }
    }
    
    /**
     * Check if current user is HR
     */
    fun isHR(): Boolean {
        return getUserRole() == UserRole.HR
    }
    
    /**
     * Check if current user is Applicant
     */
    fun isApplicant(): Boolean {
        return getUserRole() == UserRole.APPLICANT
    }
}
