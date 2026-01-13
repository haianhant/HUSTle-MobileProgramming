package com.example.hustleapp.ui.applicant.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.data.local.entity.Education
import com.example.hustleapp.databinding.ItemEducationBinding
import com.example.hustleapp.utils.DateUtils

/**
 * ListAdapter for Education items with DiffUtil
 */
class EducationAdapter(
    private val onDeleteClick: (Education) -> Unit
) : ListAdapter<Education, EducationAdapter.EducationViewHolder>(EducationDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val binding = ItemEducationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EducationViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class EducationViewHolder(
        private val binding: ItemEducationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(education: Education) {
            binding.apply {
                tvDegree.text = education.degree
                tvSchool.text = education.school
                tvDateRange.text = DateUtils.formatDateRange(education.startDate, education.endDate)
                
                btnDelete.setOnClickListener {
                    onDeleteClick(education)
                }
            }
        }
    }
    
    class EducationDiffCallback : DiffUtil.ItemCallback<Education>() {
        override fun areItemsTheSame(oldItem: Education, newItem: Education): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Education, newItem: Education): Boolean {
            return oldItem == newItem
        }
    }
}
