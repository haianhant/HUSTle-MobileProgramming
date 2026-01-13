package com.example.hustleapp.ui.applicant.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.Education
import com.example.hustleapp.data.local.entity.Experience
import com.example.hustleapp.databinding.FragmentProfileBinding
import com.example.hustleapp.ui.applicant.profile.adapters.EducationAdapter
import com.example.hustleapp.ui.applicant.profile.adapters.ExperienceAdapter
import com.google.android.material.chip.Chip

/**
 * Profile Fragment displaying user's comprehensive profile
 */
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()
    
    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var educationAdapter: EducationAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupAdapters()
        setupListeners()
        observeViewModel()
    }
    
    private fun setupAdapters() {
        experienceAdapter = ExperienceAdapter(
            onDeleteClick = { viewModel.deleteExperience(it) }
        )
        binding.rvExperience.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = experienceAdapter
        }
        
        educationAdapter = EducationAdapter(
            onDeleteClick = { viewModel.deleteEducation(it) }
        )
        binding.rvEducation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = educationAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnEdit.setOnClickListener {
            viewModel.enterEditMode()
            showEditProfileDialog()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
        
        binding.btnAddSkill.setOnClickListener {
            showAddSkillDialog()
        }
        
        binding.btnAddExperience.setOnClickListener {
            showAddExperienceDialog()
        }
        
        binding.btnAddEducation.setOnClickListener {
            showAddEducationDialog()
        }
    }
    
    private fun observeViewModel() {
        viewModel.skills.observe(viewLifecycleOwner) { skills ->
            binding.chipGroupSkills.removeAllViews()
            skills.forEach { skill ->
                val chip = Chip(requireContext()).apply {
                    text = skill.name
                    isCloseIconVisible = true
                    setOnCloseIconClickListener {
                        viewModel.deleteSkill(skill)
                    }
                    setChipBackgroundColorResource(R.color.primary_light)
                    setTextColor(resources.getColor(R.color.white, null))
                }
                binding.chipGroupSkills.addView(chip)
            }
        }
        
        viewModel.experiences.observe(viewLifecycleOwner) { experiences ->
            experienceAdapter.submitList(experiences)
        }
        
        viewModel.education.observe(viewLifecycleOwner) { educationList ->
            educationAdapter.submitList(educationList)
        }
        
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }
    
    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etHeadline = dialogView.findViewById<EditText>(R.id.etHeadline)
        val etAbout = dialogView.findViewById<EditText>(R.id.etAbout)
        
        viewModel.user.value?.let { user ->
            etName.setText(user.name)
            etHeadline.setText(user.headline ?: "")
            etAbout.setText(user.about ?: "")
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Chỉnh sửa hồ sơ")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                viewModel.setEditName(etName.text.toString())
                viewModel.setEditHeadline(etHeadline.text.toString())
                viewModel.setEditAbout(etAbout.text.toString())
                viewModel.saveProfile()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                viewModel.cancelEditMode()
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showAddSkillDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "Nhập tên kỹ năng"
            setPadding(48, 32, 48, 32)
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Thêm kỹ năng")
            .setView(editText)
            .setPositiveButton("Thêm") { _, _ ->
                viewModel.addSkill(editText.text.toString())
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showAddExperienceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_experience, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etCompany = dialogView.findViewById<EditText>(R.id.etCompany)
        val etDescription = dialogView.findViewById<EditText>(R.id.etDescription)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Thêm kinh nghiệm")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val title = etTitle.text.toString()
                val company = etCompany.text.toString()
                val description = etDescription.text.toString()
                
                if (title.isNotBlank() && company.isNotBlank()) {
                    val experience = Experience(
                        userId = HUSTleApplication.instance.sessionManager.getUserId(),
                        title = title,
                        company = company,
                        description = description.takeIf { it.isNotBlank() },
                        startDate = System.currentTimeMillis()
                    )
                    viewModel.addExperience(experience)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showAddEducationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_education, null)
        val etDegree = dialogView.findViewById<EditText>(R.id.etDegree)
        val etSchool = dialogView.findViewById<EditText>(R.id.etSchool)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Thêm học vấn")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val degree = etDegree.text.toString()
                val school = etSchool.text.toString()
                
                if (degree.isNotBlank() && school.isNotBlank()) {
                    val education = Education(
                        userId = HUSTleApplication.instance.sessionManager.getUserId(),
                        degree = degree,
                        school = school,
                        startDate = System.currentTimeMillis()
                    )
                    viewModel.addEducation(education)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc muốn đăng xuất?")
            .setPositiveButton("Đăng xuất") { _, _ ->
                viewModel.logout()
                findNavController().navigate(R.id.loginFragment)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
