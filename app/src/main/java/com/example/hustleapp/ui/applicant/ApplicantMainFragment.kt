package com.example.hustleapp.ui.applicant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hustleapp.R
import com.example.hustleapp.databinding.FragmentApplicantMainBinding
import com.example.hustleapp.ui.applicant.home.HomeFragment
import com.example.hustleapp.ui.applicant.jobs.JobsFragment
import com.example.hustleapp.ui.applicant.profile.ProfileFragment
import com.example.hustleapp.ui.applicant.roadmap.RoadmapFragment

/**
 * Main container fragment for Applicant module with bottom navigation
 */
class ApplicantMainFragment : Fragment() {
    
    private var _binding: FragmentApplicantMainBinding? = null
    private val binding get() = _binding!!
    
    private var currentFragment: Fragment? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplicantMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBottomNavigation()
        setupFragmentResultListener()
        
        // Show default fragment
        if (savedInstanceState == null) {
            showFragment(ProfileFragment())
        }
    }
    
    private fun setupFragmentResultListener() {
        // Listen for post created result to switch to community tab
        parentFragmentManager.setFragmentResultListener("post_created", viewLifecycleOwner) { _, _ ->
            binding.bottomNavigation.selectedItemId = R.id.homeFragment
        }
        
        // Listen for back from comments to switch to community tab
        parentFragmentManager.setFragmentResultListener("back_to_community", viewLifecycleOwner) { _, _ ->
            binding.bottomNavigation.selectedItemId = R.id.homeFragment
        }
        
        // Listen for back from job detail to switch to jobs tab
        parentFragmentManager.setFragmentResultListener("back_to_jobs", viewLifecycleOwner) { _, _ ->
            binding.bottomNavigation.selectedItemId = R.id.jobsFragment
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    showFragment(ProfileFragment())
                    true
                }
                R.id.homeFragment -> {
                    showFragment(HomeFragment())
                    true
                }
                R.id.jobsFragment -> {
                    showFragment(JobsFragment())
                    true
                }
                R.id.roadmapFragment -> {
                    showFragment(RoadmapFragment())
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
    
    fun navigateToJobDetail(jobId: Long) {
        val action = ApplicantMainFragmentDirections.actionApplicantMainToJobDetail(jobId)
        findNavController().navigate(action)
    }
    
    fun navigateToCreatePost() {
        findNavController().navigate(R.id.action_applicantMain_to_createPost)
    }
    
    fun navigateToPostComments(postId: Long) {
        val action = ApplicantMainFragmentDirections.actionApplicantMainToPostComments(postId)
        findNavController().navigate(action)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
