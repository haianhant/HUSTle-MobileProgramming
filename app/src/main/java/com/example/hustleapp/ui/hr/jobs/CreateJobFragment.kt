package com.example.hustleapp.ui.hr.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.databinding.FragmentCreateJobBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Create/Edit Job Fragment for HR
 */
class CreateJobFragment : Fragment() {
    
    private var _binding: FragmentCreateJobBinding? = null
    private val binding get() = _binding!!
    
    private val args: CreateJobFragmentArgs by navArgs()
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val sessionManager by lazy { HUSTleApplication.instance.sessionManager }
    
    private var isEditMode = false
    private var existingJob: Job? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateJobBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        isEditMode = args.jobId != -1L
        
        if (isEditMode) {
            loadExistingJob()
        }
        
        setupListeners()
    }
    
    private fun loadExistingJob() {
        CoroutineScope(Dispatchers.IO).launch {
            existingJob = jobRepository.getJobById(args.jobId)
            existingJob?.let { job ->
                withContext(Dispatchers.Main) {
                    binding.apply {
                        etTitle.setText(job.title)
                        etCompany.setText(job.company)
                        etLocation.setText(job.location)
                        etSalary.setText(job.salary)
                        etDescription.setText(job.description)
                        etRequirements.setText(job.requirements)
                    }
                }
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSave.setOnClickListener {
            saveJob()
        }
    }
    
    private fun saveJob() {
        val title = binding.etTitle.text.toString().trim()
        val company = binding.etCompany.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val salary = binding.etSalary.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val requirements = binding.etRequirements.text.toString().trim()
        
        // Validate
        if (title.isEmpty()) {
            binding.etTitle.error = "Vui lòng nhập tiêu đề"
            return
        }
        if (company.isEmpty()) {
            binding.etCompany.error = "Vui lòng nhập tên công ty"
            return
        }
        if (location.isEmpty()) {
            binding.etLocation.error = "Vui lòng nhập địa điểm"
            return
        }
        if (description.isEmpty()) {
            binding.etDescription.error = "Vui lòng nhập mô tả"
            return
        }
        
        binding.btnSave.isEnabled = false
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isEditMode && existingJob != null) {
                    val updatedJob = existingJob!!.copy(
                        title = title,
                        company = company,
                        location = location,
                        salary = salary,
                        description = description,
                        requirements = requirements
                    )
                    jobRepository.updateJob(updatedJob)
                } else {
                    val newJob = Job(
                        hrUserId = sessionManager.getUserId(),
                        title = title,
                        company = company,
                        location = location,
                        salary = salary,
                        description = description,
                        requirements = requirements
                    )
                    jobRepository.createJob(newJob)
                }
                
                withContext(Dispatchers.Main) {
                    val message = if (isEditMode) "Đã cập nhật việc làm" else "Đã đăng việc làm"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.btnSave.isEnabled = true
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

