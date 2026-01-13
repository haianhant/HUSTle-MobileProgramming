package com.example.hustleapp.ui.hr.jobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.data.local.entity.Job
import com.example.hustleapp.databinding.ItemJobBinding
import com.example.hustleapp.utils.DateUtils

/**
 * Adapter for HR Job list with applicant count
 */
class HRJobAdapter(
    private val onJobClick: (Job) -> Unit,
    private val onViewApplicantsClick: (Job) -> Unit
) : ListAdapter<Job, HRJobAdapter.JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JobViewHolder(
        private val binding: ItemJobBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(job: Job) {
            binding.apply {
                tvTitle.text = job.title
                tvCompany.text = job.company
                tvLocation.text = job.location
                tvSalary.text = job.salary
                tvTime.text = DateUtils.getTimeAgo(job.createdAt)
                
                root.setOnClickListener { onJobClick(job) }
                root.setOnLongClickListener { 
                    onViewApplicantsClick(job)
                    true
                }
            }
        }
    }

    class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}
