package com.example.hustleapp.ui.hr.applicants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hustleapp.HUSTleApplication
import com.example.hustleapp.R
import com.example.hustleapp.data.local.entity.Application
import com.example.hustleapp.data.local.entity.ApplicationStatus
import com.example.hustleapp.databinding.ItemApplicantBinding
import com.example.hustleapp.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Adapter for applicant list
 */
class ApplicantListAdapter(
    private val onApplicantClick: (Application) -> Unit
) : ListAdapter<Application, ApplicantListAdapter.ApplicantViewHolder>(ApplicantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val binding = ItemApplicantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ApplicantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ApplicantViewHolder(
        private val binding: ItemApplicantBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(application: Application) {
            binding.apply {
                tvAppliedTime.text = DateUtils.getTimeAgo(application.appliedAt)
                
                // Load applicant name
                CoroutineScope(Dispatchers.IO).launch {
                    val user = HUSTleApplication.instance.userRepository.getUserById(application.applicantId)
                    val job = HUSTleApplication.instance.jobRepository.getJobById(application.jobId)
                    withContext(Dispatchers.Main) {
                        tvApplicantName.text = user?.name ?: "Ứng viên"
                        tvJobTitle.text = "Ứng tuyển: ${job?.title ?: "Không xác định"}"
                    }
                }
                
                // Status
                when (application.status) {
                    ApplicationStatus.PENDING -> {
                        tvStatus.text = "Chờ xử lý"
                        tvStatus.setTextColor(root.context.getColor(R.color.status_pending))
                    }
                    ApplicationStatus.SHORTLISTED -> {
                        tvStatus.text = "Đã chọn"
                        tvStatus.setTextColor(root.context.getColor(R.color.status_shortlisted))
                    }
                    ApplicationStatus.REJECTED -> {
                        tvStatus.text = "Từ chối"
                        tvStatus.setTextColor(root.context.getColor(R.color.status_rejected))
                    }
                }
                
                root.setOnClickListener { onApplicantClick(application) }
            }
        }
    }

    class ApplicantDiffCallback : DiffUtil.ItemCallback<Application>() {
        override fun areItemsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem == newItem
        }
    }
}
