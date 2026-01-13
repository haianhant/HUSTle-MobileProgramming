package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.databinding.FragmentJobApplicantsBinding

/**
 * Job Applicants Fragment showing applicants for a specific job
 */
class JobApplicantsFragment : Fragment() {
    
    private var _binding: FragmentJobApplicantsBinding? = null
    private val binding get() = _binding!!
    
    private val args: JobApplicantsFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobApplicantsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
