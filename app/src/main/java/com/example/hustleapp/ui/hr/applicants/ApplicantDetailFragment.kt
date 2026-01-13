package com.example.hustleapp.ui.hr.applicants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.hustleapp.databinding.FragmentApplicantDetailBinding

/**
 * Applicant Detail Fragment showing full applicant profile
 */
class ApplicantDetailFragment : Fragment() {
    
    private var _binding: FragmentApplicantDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: ApplicantDetailFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplicantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
