package com.example.hustleapp.ui.applicant.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hustleapp.databinding.FragmentCreatePostBinding

/**
 * Create Post Fragment for composing new posts
 */
class CreatePostFragment : Fragment() {
    
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
        observeViewModel()
    }
    
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            // Set result so parent knows to show community tab when going back
            parentFragmentManager.setFragmentResult("back_to_community", Bundle())
            findNavController().navigateUp()
        }
        
        binding.btnPost.setOnClickListener {
            val content = binding.etContent.text.toString()
            viewModel.createPost(content)
        }
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnPost.isEnabled = !isLoading
        }
        
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
                if (it.contains("Đã đăng")) {
                    // Set result so parent knows to show community tab
                    parentFragmentManager.setFragmentResult("post_created", Bundle())
                    findNavController().navigateUp()
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
