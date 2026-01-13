package com.example.hustleapp.ui.hr.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hustleapp.databinding.FragmentHrJobsBinding
import com.example.hustleapp.ui.hr.HRMainFragment

class HRJobsFragment : Fragment() {
    
    private var _binding: FragmentHrJobsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHrJobsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.fabCreateJob.setOnClickListener {
            (parentFragment as? HRMainFragment)?.navigateToCreateJob()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
