package com.example.hustleapp.ui.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hustleapp.R
import com.example.hustleapp.databinding.FragmentHrMainBinding
import com.example.hustleapp.ui.hr.analytics.AnalyticsFragment
import com.example.hustleapp.ui.hr.applicants.ApplicantsFragment
import com.example.hustleapp.ui.hr.jobs.HRJobsFragment

/**
 * Main container fragment for HR module with bottom navigation
 */
class HRMainFragment : Fragment() {
    
    private var _binding: FragmentHrMainBinding? = null
    private val binding get() = _binding!!
    
    private var currentFragment: Fragment? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHrMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBottomNavigation()
        
        // Show default fragment
        if (savedInstanceState == null) {
            showFragment(HRJobsFragment())
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.hrJobsFragment -> {
                    showFragment(HRJobsFragment())
                    true
                }
                R.id.applicantsFragment -> {
                    showFragment(ApplicantsFragment())
                    true
                }
                R.id.analyticsFragment -> {
                    showFragment(AnalyticsFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun showFragment(fragment: Fragment) {
        if (currentFragment?.javaClass == fragment.javaClass) return
        
        currentFragment = fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.tabContainer, fragment)
            .commit()
    }
    
    fun navigateToCreateJob(jobId: Long = -1L) {
        val action = HRMainFragmentDirections.actionHrMainToCreateJob(jobId)
        findNavController().navigate(action)
    }
    
    fun navigateToApplicantDetail(applicationId: Long) {
        val action = HRMainFragmentDirections.actionHrMainToApplicantDetail(applicationId)
        findNavController().navigate(action)
    }
    
    fun navigateToJobApplicants(jobId: Long) {
        val action = HRMainFragmentDirections.actionHrMainToJobApplicants(jobId)
        findNavController().navigate(action)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
