package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.databinding.FragmentApplicantDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Applicant Detail Fragment showing full applicant profile with status actions
 */
class ApplicantDetailFragment : Fragment() {
    
    private var _binding: FragmentApplicantDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: ApplicantDetailFragmentArgs by navArgs()
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val userRepository by lazy { HUSTleApplication.instance.userRepository }
    
    private var currentApplication: Application? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplicantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
        loadApplicantDetails()
    }
    
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnShortlist.setOnClickListener {
            updateStatus(ApplicationStatus.SHORTLISTED)
        }
        
        binding.btnReject.setOnClickListener {
            updateStatus(ApplicationStatus.REJECTED)
        }
    }
    
    private fun loadApplicantDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val application = jobRepository.getApplicationById(args.applicationId)
                currentApplication = application
                
                application?.let { app ->
                    val user = userRepository.getUserById(app.applicantId)
                    val job = jobRepository.getJobById(app.jobId)
                    
                    withContext(Dispatchers.Main) {
                        binding.tvName.text = user?.name ?: "Không xác định"
                        binding.tvEmail.text = user?.email ?: "Không có email"
                        binding.tvJobApplied.text = "Ứng tuyển: ${job?.title ?: "Không xác định"}"
                        updateStatusUI(app.status)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun updateStatus(newStatus: ApplicationStatus) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                jobRepository.updateApplicationStatus(args.applicationId, newStatus)
                
                withContext(Dispatchers.Main) {
                    updateStatusUI(newStatus)
                    val message = when (newStatus) {
                        ApplicationStatus.SHORTLISTED -> "Đã chọn ứng viên"
                        ApplicationStatus.REJECTED -> "Đã từ chối ứng viên"
                        else -> "Đã cập nhật"
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun updateStatusUI(status: ApplicationStatus) {
        when (status) {
            ApplicationStatus.PENDING -> {
                binding.tvStatus.text = "Chờ xử lý"
                binding.tvStatus.setTextColor(requireContext().getColor(R.color.status_pending))
                binding.btnShortlist.isEnabled = true
                binding.btnReject.isEnabled = true
            }
            ApplicationStatus.SHORTLISTED -> {
                binding.tvStatus.text = "Đã chọn"
                binding.tvStatus.setTextColor(requireContext().getColor(R.color.status_shortlisted))
                binding.btnShortlist.isEnabled = false
                binding.btnReject.isEnabled = true
            }
            ApplicationStatus.REJECTED -> {
                binding.tvStatus.text = "Đã từ chối"
                binding.tvStatus.setTextColor(requireContext().getColor(R.color.status_rejected))
                binding.btnShortlist.isEnabled = true
                binding.btnReject.isEnabled = false
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


