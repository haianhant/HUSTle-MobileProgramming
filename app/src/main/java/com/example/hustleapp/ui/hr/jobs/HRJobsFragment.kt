package com.example.hustleapp.ui.hr.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.databinding.FragmentHrJobsBinding
import com.example.hustleapp.ui.hr.HRMainFragment

class HRJobsFragment : Fragment() {
    
    private var _binding: FragmentHrJobsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var jobAdapter: HRJobAdapter
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val sessionManager by lazy { HUSTleApplication.instance.sessionManager }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHrJobsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        observeJobs()
    }
    
    private fun setupRecyclerView() {
        jobAdapter = HRJobAdapter(
            onJobClick = { job ->
                // Navigate to edit job
                (parentFragment as? HRMainFragment)?.navigateToCreateJob(job.id)
            },
            onViewApplicantsClick = { job ->
                // Navigate to view applicants for this job
                (parentFragment as? HRMainFragment)?.navigateToJobApplicants(job.id)
            }
        )
        
        binding.rvJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }
    }
    
    private fun setupListeners() {
        binding.fabCreateJob.setOnClickListener {
            (parentFragment as? HRMainFragment)?.navigateToCreateJob()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }
    
    private fun showLogoutConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng xuất") { _, _ ->
                sessionManager.clearSession()
                // Navigate to login using parent fragment's nav controller
                androidx.navigation.Navigation.findNavController(requireActivity(), com.example.hustleapp.R.id.nav_host_fragment)
                    .navigate(
                        com.example.hustleapp.R.id.loginFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(com.example.hustleapp.R.id.nav_graph, true)
                            .build()
                    )
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun observeJobs() {
        jobRepository.getJobsByHrId(sessionManager.getUserId()).observe(viewLifecycleOwner) { jobs ->
            jobAdapter.submitList(jobs)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

