package com.example.hustleapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.UserRole
import com.example.hustleapp.databinding.FragmentRegisterBinding

/**
 * Register Fragment for new user registration
 */
class RegisterFragment : Fragment() {
    
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: RegisterViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
        observeViewModel()
    }
    
    private fun setupListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        // Text change listeners
        binding.etName.addTextChangedListener {
            viewModel.setName(it.toString())
            viewModel.clearError()
        }
        
        binding.etEmail.addTextChangedListener {
            viewModel.setEmail(it.toString())
            viewModel.clearError()
        }
        
        binding.etPassword.addTextChangedListener {
            viewModel.setPassword(it.toString())
            viewModel.clearError()
        }
        
        binding.etConfirmPassword.addTextChangedListener {
            viewModel.setConfirmPassword(it.toString())
            viewModel.clearError()
        }
        
        // Role selection
        binding.rgRole.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbApplicant -> viewModel.setRole(UserRole.APPLICANT)
                R.id.rbHR -> viewModel.setRole(UserRole.HR)
            }
        }
        
        // Register button
        binding.btnRegister.setOnClickListener {
            viewModel.register()
        }
        
        // Navigate to login
        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun observeViewModel() {
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
        }
        
        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
        
        // Observe registration success
        viewModel.registerSuccess.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Toast.makeText(requireContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                navigateToMain(user.role)
            }
        }
    }
    
    private fun navigateToMain(role: UserRole) {
        when (role) {
            UserRole.APPLICANT -> {
                findNavController().navigate(R.id.action_registerFragment_to_applicantNavigation)
            }
            UserRole.HR -> {
                findNavController().navigate(R.id.action_registerFragment_to_hrNavigation)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
