package com.example.hustleapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.data.local.entity.JobStatus

@Dao
interface JobDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: Job): Long
    
    @Update
    suspend fun update(job: Job)
    
    @Delete
    suspend fun delete(job: Job)
    
    @Query("SELECT * FROM jobs WHERE id = :jobId")
    suspend fun getJobById(jobId: Long): Job?
    
    @Query("SELECT * FROM jobs WHERE id = :jobId")
    fun getJobByIdLiveData(jobId: Long): LiveData<Job?>
    
    @Query("SELECT * FROM jobs WHERE status = :status ORDER BY createdAt DESC")
    fun getJobsByStatus(status: JobStatus): LiveData<List<Job>>
    
    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobs(): LiveData<List<Job>>
    
    @Query("SELECT * FROM jobs WHERE status = 'OPEN' ORDER BY createdAt DESC")
    fun getOpenJobs(): LiveData<List<Job>>
    
    @Query("SELECT * FROM jobs WHERE hrUserId = :hrUserId ORDER BY createdAt DESC")
    fun getJobsByHrId(hrUserId: Long): LiveData<List<Job>>
    
    @Query("SELECT * FROM jobs WHERE title LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchJobs(query: String): LiveData<List<Job>>
    
    @Query("UPDATE jobs SET viewCount = viewCount + 1 WHERE id = :jobId")
    suspend fun incrementViewCount(jobId: Long)
    
    @Query("UPDATE jobs SET status = :status WHERE id = :jobId")
    suspend fun updateJobStatus(jobId: Long, status: JobStatus)
    
    @Query("SELECT COUNT(*) FROM jobs WHERE hrUserId = :hrUserId")
    suspend fun getJobCountByHr(hrUserId: Long): Int
    
    @Query("SELECT SUM(viewCount) FROM jobs WHERE hrUserId = :hrUserId")
    suspend fun getTotalViewsByHr(hrUserId: Long): Int?
}
