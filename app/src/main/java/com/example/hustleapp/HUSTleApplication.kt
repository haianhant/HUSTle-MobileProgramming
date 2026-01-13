package com.example.hustleapp

import android.app.Application
import com.example.hustleapp.data.local.database.HUSTleDatabase
import com.example.hustleapp.data.repository.JobRepository
import com.example.hustleapp.data.repository.PostRepository
import com.example.hustleapp.data.repository.RoadmapRepository
import com.example.hustleapp.data.repository.UserRepository
import com.example.hustleapp.utils.SessionManager

/**
 * Application class for initializing app-wide dependencies
 */
class HUSTleApplication : Application() {
    
    // Database instance
    val database by lazy { HUSTleDatabase.getDatabase(this) }
    
    // Repositories
    val userRepository by lazy {
        UserRepository(
            database.userDao(),
            database.skillDao(),
            database.experienceDao(),
            database.educationDao()
        )
    }
    
    val jobRepository by lazy {
        JobRepository(
            database.jobDao(),
            database.applicationDao()
        )
    }
    
    val postRepository by lazy {
        PostRepository(
            database.postDao(),
            database.commentDao()
        )
    }
    
    val roadmapRepository by lazy {
        RoadmapRepository(
            database.roadmapDao()
        )
    }
    
    // Session manager
    val sessionManager by lazy { SessionManager(this) }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: HUSTleApplication
            private set
    }
}
