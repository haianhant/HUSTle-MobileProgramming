package com.example.hustleapp.ui.applicant.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.databinding.FragmentJobsBinding
import com.example.hustleapp.ui.applicant.ApplicantMainFragment
import com.example.hustleapp.ui.applicant.jobs.adapters.JobAdapter

/**
 * Jobs Fragment displaying job listings
 */
class JobsFragment : Fragment() {
    
    private var _binding: FragmentJobsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: JobsViewModel by viewModels()
    private lateinit var jobAdapter: JobAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        jobAdapter = JobAdapter { job ->
            viewModel.incrementJobView(job.id)
            (parentFragment as? ApplicantMainFragment)?.navigateToJobDetail(job.id)
        }
        
        binding.rvJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }
    }
    
    private fun setupListeners() {
        binding.etSearch.addTextChangedListener { editable ->
            val query = editable.toString()
            if (query.isNotBlank()) {
                viewModel.searchJobs(query).observe(viewLifecycleOwner) { jobs ->
                    jobAdapter.submitList(jobs)
                }
            } else {
                viewModel.jobs.observe(viewLifecycleOwner) { jobs ->
                    jobAdapter.submitList(jobs)
                }
            }
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }
    
    private fun observeViewModel() {
        viewModel.jobs.observe(viewLifecycleOwner) { jobs ->
            jobAdapter.submitList(jobs)
            binding.emptyState.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE
        }
        
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
