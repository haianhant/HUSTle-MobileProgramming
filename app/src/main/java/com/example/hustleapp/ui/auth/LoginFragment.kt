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
import com.example.hustleapp.databinding.FragmentLoginBinding

/**
 * Login Fragment for user authentication
 */
class LoginFragment : Fragment() {
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Check existing session
        if (viewModel.checkExistingSession()) {
            navigateToMain()
            return
        }
        
        setupListeners()
        observeViewModel()
    }
    
    private fun setupListeners() {
        // Two-way binding for email
        binding.etEmail.addTextChangedListener { 
            viewModel.setEmail(it.toString())
            viewModel.clearError()
        }
        
        // Two-way binding for password
        binding.etPassword.addTextChangedListener { 
            viewModel.setPassword(it.toString())
            viewModel.clearError()
        }
        
        // Login button
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
        
        // Navigate to register
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
    
    private fun observeViewModel() {
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
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
        
        // Observe login success
        viewModel.loginSuccess.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }
        }
    }
    
    private fun navigateToMain() {
        val role = viewModel.getCurrentUserRole()
        when (role) {
            UserRole.APPLICANT -> {
                findNavController().navigate(R.id.action_loginFragment_to_applicantNavigation)
            }
            UserRole.HR -> {
                findNavController().navigate(R.id.action_loginFragment_to_hrNavigation)
            }
            else -> {
                // Should not happen, stay on login
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
