package com.example.hustleapp.ui.applicant.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Comment
import com.example.hustleapp.databinding.FragmentPostCommentsBinding
import com.example.hustleapp.ui.applicant.home.adapters.CommentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Post Comments Fragment for viewing and adding comments
 */
class PostCommentsFragment : Fragment() {
    
    private var _binding: FragmentPostCommentsBinding? = null
    private val binding get() = _binding!!
    
    private val args: PostCommentsFragmentArgs by navArgs()
    
    private lateinit var commentAdapter: CommentAdapter
    
    private val postRepository by lazy { HUSTleApplication.instance.postRepository }
    private val sessionManager by lazy { HUSTleApplication.instance.sessionManager }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupListeners()
        observeComments()
    }
    
    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter()
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }
    }
    
    private fun setupListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            // Set result so parent knows to show community tab
            parentFragmentManager.setFragmentResult("back_to_community", Bundle())
            findNavController().navigateUp()
        }
        
        // Send comment button
        binding.btnSend.setOnClickListener {
            val content = binding.etComment.text.toString().trim()
            if (content.isNotEmpty()) {
                sendComment(content)
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun sendComment(content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val comment = Comment(
                    postId = args.postId,
                    authorId = sessionManager.getUserId(),
                    content = content
                )
                postRepository.addComment(comment)
                
                withContext(Dispatchers.Main) {
                    binding.etComment.text?.clear()
                    Toast.makeText(requireContext(), "Đã gửi bình luận", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun observeComments() {
        postRepository.getCommentsByPostId(args.postId).observe(viewLifecycleOwner) { comments ->
            commentAdapter.submitList(comments)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

