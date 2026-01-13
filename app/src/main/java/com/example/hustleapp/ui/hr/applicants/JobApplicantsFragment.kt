package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.databinding.FragmentJobApplicantsBinding

/**
 * Job Applicants Fragment showing applicants for a specific job
 */
class JobApplicantsFragment : Fragment() {
    
    private var _binding: FragmentJobApplicantsBinding? = null
    private val binding get() = _binding!!
    
    private val args: JobApplicantsFragmentArgs by navArgs()
    
    private lateinit var applicantAdapter: ApplicantListAdapter
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobApplicantsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        observeApplicants()
    }
    
    private fun setupRecyclerView() {
        applicantAdapter = ApplicantListAdapter { application ->
            // Navigate to applicant detail
            val action = JobApplicantsFragmentDirections
                .actionJobApplicantsToApplicantDetail(application.id)
            findNavController().navigate(action)
        }
        
        binding.rvApplicants.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = applicantAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun observeApplicants() {
        jobRepository.getApplicationsByJobId(args.jobId).observe(viewLifecycleOwner) { applications ->
            applicantAdapter.submitList(applications)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

