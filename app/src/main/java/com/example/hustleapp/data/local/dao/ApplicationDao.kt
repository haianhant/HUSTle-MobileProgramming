package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus

@Dao
interface ApplicationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: Application): Long
    
    @Update
    suspend fun update(application: Application)
    
    @Delete
    suspend fun delete(application: Application)
    
    @Query("SELECT * FROM applications WHERE id = :applicationId")
    suspend fun getApplicationById(applicationId: Long): Application?
    
    @Query("SELECT * FROM applications WHERE jobId = :jobId ORDER BY appliedAt DESC")
    fun getApplicationsByJobId(jobId: Long): LiveData<List<Application>>
    
    @Query("SELECT * FROM applications WHERE applicantId = :applicantId ORDER BY appliedAt DESC")
    fun getApplicationsByApplicantId(applicantId: Long): LiveData<List<Application>>
    
    @Query("SELECT * FROM applications WHERE jobId = :jobId AND applicantId = :applicantId LIMIT 1")
    suspend fun getApplicationByJobAndApplicant(jobId: Long, applicantId: Long): Application?
    
    @Query("SELECT EXISTS(SELECT 1 FROM applications WHERE jobId = :jobId AND applicantId = :applicantId)")
    suspend fun hasApplied(jobId: Long, applicantId: Long): Boolean
    
    @Query("UPDATE applications SET status = :status WHERE id = :applicationId")
    suspend fun updateStatus(applicationId: Long, status: ApplicationStatus)
    
    @Query("SELECT COUNT(*) FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId)")
    suspend fun getTotalApplicationsByHr(hrUserId: Long): Int
    
    @Query("SELECT COUNT(*) FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId) AND status = :status")
    suspend fun getApplicationCountByStatusAndHr(hrUserId: Long, status: ApplicationStatus): Int
    
    @Query("SELECT COUNT(*) FROM applications WHERE jobId = :jobId")
    suspend fun getApplicationCountByJob(jobId: Long): Int
    
    @Query("SELECT COUNT(*) FROM applications WHERE jobId = :jobId AND status = :status")
    suspend fun getApplicationCountByJobAndStatus(jobId: Long, status: ApplicationStatus): Int
    
    @Query("SELECT * FROM applications WHERE jobId IN (SELECT id FROM jobs WHERE hrUserId = :hrUserId) ORDER BY appliedAt DESC")
    fun getAllApplicationsByHr(hrUserId: Long): LiveData<List<Application>>
}
