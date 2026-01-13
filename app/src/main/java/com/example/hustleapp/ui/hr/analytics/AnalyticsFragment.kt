package com.example.hustleapp.ui.hr.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.databinding.FragmentAnalyticsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnalyticsFragment : Fragment() {
    
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val sessionManager by lazy { HUSTleApplication.instance.sessionManager }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadAnalytics()
    }
    
    private fun loadAnalytics() {
        val hrUserId = sessionManager.getUserId()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Load analytics data from database
                val totalJobs = jobRepository.getJobCountByHr(hrUserId)
                val totalApplications = jobRepository.getTotalApplicationsByHr(hrUserId)
                val totalViews = jobRepository.getTotalViewsByHr(hrUserId)
                
                val pendingCount = jobRepository.getApplicationCountByStatusAndHr(hrUserId, ApplicationStatus.PENDING)
                val shortlistedCount = jobRepository.getApplicationCountByStatusAndHr(hrUserId, ApplicationStatus.SHORTLISTED)
                val rejectedCount = jobRepository.getApplicationCountByStatusAndHr(hrUserId, ApplicationStatus.REJECTED)
                
                withContext(Dispatchers.Main) {
                    binding.apply {
                        tvTotalJobs.text = totalJobs.toString()
                        tvTotalApplications.text = totalApplications.toString()
                        tvTotalViews.text = totalViews.toString()
                        
                        tvPendingCount.text = pendingCount.toString()
                        tvShortlistedCount.text = shortlistedCount.toString()
                        tvRejectedCount.text = rejectedCount.toString()
                    }
                }
            } catch (e: Exception) {
                // Handle error silently - keep default 0 values
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
