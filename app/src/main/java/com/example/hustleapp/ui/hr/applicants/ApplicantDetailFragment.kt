package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.data.local.entity.Experience
import com.example.hustleapp.data.local.entity.Skill
import com.example.hustleapp.databinding.FragmentApplicantDetailBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Applicant Detail Fragment showing full applicant profile with status actions
 */
class ApplicantDetailFragment : Fragment() {
    
    private var _binding: FragmentApplicantDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: ApplicantDetailFragmentArgs by navArgs()
    
    private val jobRepository by lazy { HUSTleApplication.instance.jobRepository }
    private val userRepository by lazy { HUSTleApplication.instance.userRepository }
    private val skillDao by lazy { HUSTleApplication.instance.database.skillDao() }
    private val experienceDao by lazy { HUSTleApplication.instance.database.experienceDao() }
    
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
            // Set result so parent knows to show applicants tab when going back
            parentFragmentManager.setFragmentResult("back_to_applicants", Bundle())
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
                    val skills = skillDao.getSkillsByUserIdSync(app.applicantId)
                    val experiences = experienceDao.getExperiencesByUserIdSync(app.applicantId)
                    
                    withContext(Dispatchers.Main) {
                        // Basic info
                        binding.tvName.text = user?.name ?: "Không xác định"
                        binding.tvEmail.text = user?.email ?: "Không có email"
                        binding.tvJobApplied.text = "Ứng tuyển: ${job?.title ?: "Không xác định"}"
                        
                        // Headline
                        if (!user?.headline.isNullOrBlank()) {
                            binding.tvHeadline.text = user?.headline
                            binding.tvHeadline.visibility = View.VISIBLE
                        }
                        
                        // About section
                        if (!user?.about.isNullOrBlank()) {
                            binding.tvAbout.text = user?.about
                            binding.cardAbout.visibility = View.VISIBLE
                        }
                        
                        // Skills section
                        if (skills.isNotEmpty()) {
                            displaySkills(skills)
                            binding.cardSkills.visibility = View.VISIBLE
                        }
                        
                        // Experience section
                        if (experiences.isNotEmpty()) {
                            displayExperiences(experiences)
                            binding.cardExperience.visibility = View.VISIBLE
                        }
                        
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
    
    private fun displaySkills(skills: List<Skill>) {
        binding.chipGroupSkills.removeAllViews()
        for (skill in skills) {
            val chip = Chip(requireContext()).apply {
                text = skill.name
                isClickable = false
                setChipBackgroundColorResource(R.color.primary_light)
                setTextColor(resources.getColor(R.color.primary, null))
            }
            binding.chipGroupSkills.addView(chip)
        }
    }
    
    private fun displayExperiences(experiences: List<Experience>) {
        binding.layoutExperiences.removeAllViews()
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        
        for ((index, exp) in experiences.withIndex()) {
            val expView = layoutInflater.inflate(R.layout.item_experience_simple, binding.layoutExperiences, false)
            
            expView.findViewById<TextView>(R.id.tvTitle).text = exp.title
            expView.findViewById<TextView>(R.id.tvCompany).text = exp.company
            
            val startDate = dateFormat.format(Date(exp.startDate))
            val endDate = exp.endDate?.let { dateFormat.format(Date(it)) } ?: "Hiện tại"
            expView.findViewById<TextView>(R.id.tvDuration).text = "$startDate - $endDate"
            
            exp.description?.let { desc ->
                if (desc.isNotBlank()) {
                    expView.findViewById<TextView>(R.id.tvDescription).apply {
                        text = desc
                        visibility = View.VISIBLE
                    }
                }
            }
            
            binding.layoutExperiences.addView(expView)
            
            // Add divider if not last item
            if (index < experiences.size - 1) {
                val divider = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        topMargin = resources.getDimensionPixelSize(R.dimen.spacing_sm)
                        bottomMargin = resources.getDimensionPixelSize(R.dimen.spacing_sm)
                    }
                    setBackgroundColor(resources.getColor(R.color.divider, null))
                }
                binding.layoutExperiences.addView(divider)
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
