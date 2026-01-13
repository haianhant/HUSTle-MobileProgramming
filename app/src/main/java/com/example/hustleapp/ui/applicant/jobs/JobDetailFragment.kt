package com.example.hustleapp.ui.applicant.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.databinding.FragmentJobDetailBinding
import kotlinx.coroutines.launch

/**
 * Job Detail Fragment showing full job information
 */
class JobDetailFragment : Fragment() {
    
    private var _binding: FragmentJobDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: JobDetailFragmentArgs by navArgs()
    private val viewModel: JobsViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadJobDetails()
        setupListeners()
        observeViewModel()
    }
    
    private fun loadJobDetails() {
        lifecycleScope.launch {
            val job = HUSTleApplication.instance.jobRepository.getJobById(args.jobId)
            job?.let {
                binding.tvTitle.text = it.title
                binding.tvCompany.text = it.company
                binding.tvLocation.text = it.location
                binding.tvSalary.text = it.salary
                binding.tvDescription.text = it.description
                binding.tvRequirements.text = it.requirements
            }
            
            // Check application status
            loadApplicationStatus()
        }
    }
    
    private suspend fun loadApplicationStatus() {
        val hasApplied = viewModel.hasApplied(args.jobId)
        
        if (hasApplied) {
            binding.btnApply.isEnabled = false
            binding.btnApply.text = "Đã ứng tuyển"
            
            // Get application status
            val application = viewModel.getApplication(args.jobId)
            application?.let { app ->
                binding.cardApplicationStatus.visibility = View.VISIBLE
                updateStatusUI(app.status)
            }
        } else {
            binding.btnApply.isEnabled = true
            binding.btnApply.text = getString(R.string.apply)
            binding.cardApplicationStatus.visibility = View.GONE
        }
    }
    
    private fun updateStatusUI(status: ApplicationStatus) {
        when (status) {
            ApplicationStatus.PENDING -> {
                binding.tvApplicationStatus.text = "Chờ xử lý"
                binding.tvApplicationStatus.setTextColor(requireContext().getColor(R.color.status_pending))
            }
            ApplicationStatus.SHORTLISTED -> {
                binding.tvApplicationStatus.text = "Đã được chọn"
                binding.tvApplicationStatus.setTextColor(requireContext().getColor(R.color.status_shortlisted))
            }
            ApplicationStatus.REJECTED -> {
                binding.tvApplicationStatus.text = "Đã bị từ chối"
                binding.tvApplicationStatus.setTextColor(requireContext().getColor(R.color.status_rejected))
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            // Set result so parent knows to show jobs tab
            parentFragmentManager.setFragmentResult("back_to_jobs", Bundle())
            findNavController().navigateUp()
        }
        
        binding.btnApply.setOnClickListener {
            viewModel.applyForJob(args.jobId)
        }
    }
    
    private fun observeViewModel() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
                if (it.contains("thành công")) {
                    binding.btnApply.isEnabled = false
                    binding.btnApply.text = "Đã ứng tuyển"
                    // Show pending status
                    binding.cardApplicationStatus.visibility = View.VISIBLE
                    updateStatusUI(ApplicationStatus.PENDING)
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

