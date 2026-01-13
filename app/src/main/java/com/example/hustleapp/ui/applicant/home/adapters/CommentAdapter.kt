package com.example.hustleapp.ui.applicant.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.data.local.entity.Comment
import com.example.hustleapp.databinding.ItemCommentBinding
import com.example.hustleapp.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ListAdapter for Comment items with DiffUtil
 */
class CommentAdapter : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(comment: Comment) {
            binding.apply {
                tvContent.text = comment.content
                tvTime.text = DateUtils.getTimeAgo(comment.createdAt)
                
                // Load author name
                CoroutineScope(Dispatchers.IO).launch {
                    val user = HUSTleApplication.instance.userRepository.getUserById(comment.authorId)
                    withContext(Dispatchers.Main) {
                        tvAuthorName.text = user?.name ?: "Người dùng"
                    }
                }
            }
        }
    }
    
    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}
