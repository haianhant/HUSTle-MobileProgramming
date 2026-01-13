package com.example.hustleapp.ui.applicant.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.Post
import com.example.hustleapp.databinding.ItemPostBinding
import com.example.hustleapp.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ListAdapter for Post items with DiffUtil
 */
class PostAdapter(
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit,
    private val onShareClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class PostViewHolder(
        private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(post: Post) {
            binding.apply {
                tvContent.text = post.content
                tvLikeCount.text = post.likeCount.toString()
                tvCommentCount.text = post.commentCount.toString()
                tvTime.text = DateUtils.getTimeAgo(post.createdAt)
                
                // Load author name
                CoroutineScope(Dispatchers.IO).launch {
                    val user = HUSTleApplication.instance.userRepository.getUserById(post.authorId)
                    withContext(Dispatchers.Main) {
                        tvAuthorName.text = user?.name ?: "Người dùng"
                    }
                }
                
                // Image visibility
                if (post.imageUrl.isNullOrBlank()) {
                    ivPostImage.visibility = View.GONE
                } else {
                    ivPostImage.visibility = View.VISIBLE
                }
                
                // Check if liked
                CoroutineScope(Dispatchers.IO).launch {
                    val hasLiked = HUSTleApplication.instance.postRepository.hasUserLikedPost(
                        post.id, 
                        HUSTleApplication.instance.sessionManager.getUserId()
                    )
                    withContext(Dispatchers.Main) {
                        if (hasLiked) {
                            ivLike.setImageResource(R.drawable.ic_favorite)
                            ivLike.setColorFilter(root.context.getColor(R.color.primary))
                        } else {
                            ivLike.setImageResource(R.drawable.ic_favorite_border)
                            ivLike.setColorFilter(root.context.getColor(R.color.text_secondary))
                        }
                    }
                }
                
                btnLike.setOnClickListener { onLikeClick(post) }
                btnComment.setOnClickListener { onCommentClick(post) }
                btnShare.setOnClickListener { onShareClick(post) }
            }
        }
    }
    
    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
