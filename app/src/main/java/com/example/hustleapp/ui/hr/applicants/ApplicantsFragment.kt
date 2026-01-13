package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.databinding.FragmentApplicantsBinding
import com.example.hustleapp.ui.hr.HRMainFragment

class ApplicantsFragment : Fragment() {
    
    private var _binding: FragmentApplicantsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var applicantAdapter: ApplicantListAdapter
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val sessionManager by lazy { HUSTleApplication.instance.sessionManager }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplicantsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeApplicants()
    }
    
    private fun setupRecyclerView() {
        applicantAdapter = ApplicantListAdapter { application ->
            // Navigate to applicant detail
            (parentFragment as? HRMainFragment)?.navigateToApplicantDetail(application.id)
        }
        
        binding.rvApplicants.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = applicantAdapter
        }
    }
    
    private fun observeApplicants() {
        jobRepository.getAllApplicationsByHr(sessionManager.getUserId()).observe(viewLifecycleOwner) { applications ->
            applicantAdapter.submitList(applications)
            
            if (applications.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvApplicants.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvApplicants.visibility = View.VISIBLE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
