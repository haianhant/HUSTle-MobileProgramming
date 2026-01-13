package com.example.hustleapp.ui.applicant.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.data.local.entity.Experience
import com.example.hustleapp.databinding.ItemExperienceBinding
import com.example.hustleapp.utils.DateUtils

/**
 * ListAdapter for Experience items with DiffUtil
 */
class ExperienceAdapter(
    private val onDeleteClick: (Experience) -> Unit
) : ListAdapter<Experience, ExperienceAdapter.ExperienceViewHolder>(ExperienceDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val binding = ItemExperienceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExperienceViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ExperienceViewHolder(
        private val binding: ItemExperienceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(experience: Experience) {
            binding.apply {
                tvTitle.text = experience.title
                tvCompany.text = experience.company
                tvDateRange.text = DateUtils.formatDateRange(experience.startDate, experience.endDate)
                tvDescription.text = experience.description ?: ""
                
                btnDelete.setOnClickListener {
                    onDeleteClick(experience)
                }
            }
        }
    }
    
    class ExperienceDiffCallback : DiffUtil.ItemCallback<Experience>() {
        override fun areItemsTheSame(oldItem: Experience, newItem: Experience): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Experience, newItem: Experience): Boolean {
            return oldItem == newItem
        }
    }
}
