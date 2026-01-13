package com.example.hustleapp.ui.applicant.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.databinding.FragmentPostCommentsBinding

/**
 * Post Comments Fragment for viewing and adding comments
 */
class PostCommentsFragment : Fragment() {
    
    private var _binding: FragmentPostCommentsBinding? = null
    private val binding get() = _binding!!
    
    private val args: PostCommentsFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
