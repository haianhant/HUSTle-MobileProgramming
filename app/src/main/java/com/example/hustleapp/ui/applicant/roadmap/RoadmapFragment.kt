package com.example.hustleapp.ui.applicant.roadmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hustleapp.databinding.FragmentRoadmapBinding

/**
 * Roadmap Fragment with Gemini AI integration for career development planning
 */
class RoadmapFragment : Fragment() {
    
    private var _binding: FragmentRoadmapBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: RoadmapViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoadmapBinding.inflate(inflater, container, false)
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
        // Generate button
        binding.btnGenerate.setOnClickListener {
            viewModel.generateRoadmap()
        }
        
        // Reset button
        binding.btnReset.setOnClickListener {
            viewModel.resetRoadmap()
        }
        
        // Years experience input
        binding.etYearsExperience.addTextChangedListener { editable ->
            val years = editable.toString().toIntOrNull() ?: 0
            viewModel.setYearsExperience(years)
        }
        
        // Two-way binding for text inputs (already done via @={} in XML)
    }
    
    private fun observeViewModel() {
        // Observe roadmap steps and update flowchart
        viewModel.roadmapSteps.observe(viewLifecycleOwner) { steps ->
            binding.flowchartView.setSteps(steps)
        }
        
        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
