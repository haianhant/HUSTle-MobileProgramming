package com.example.hustleapp.ui.applicant.roadmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hustleapp.databinding.FragmentRoadmapBinding

/**
 * Roadmap Fragment for career development tracking
 */
class RoadmapFragment : Fragment() {
    
    private var _binding: FragmentRoadmapBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoadmapBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
