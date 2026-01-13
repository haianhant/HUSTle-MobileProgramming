package com.example.hustleapp.ui.applicant.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.databinding.FragmentHomeBinding
import com.example.hustleapp.ui.applicant.ApplicantMainFragment
import com.example.hustleapp.ui.applicant.home.adapters.PostAdapter

/**
 * Home Fragment displaying social feed
 */
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        postAdapter = PostAdapter(
            onLikeClick = { post -> viewModel.toggleLike(post.id) },
            onCommentClick = { post -> 
                (parentFragment as? ApplicantMainFragment)?.navigateToPostComments(post.id)
            },
            onShareClick = { /* Share functionality */ }
        )
        
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }
    
    private fun setupListeners() {
        binding.fabCreatePost.setOnClickListener {
            (parentFragment as? ApplicantMainFragment)?.navigateToCreatePost()
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }
    
    private fun observeViewModel() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
            binding.emptyState.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
        }
        
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
