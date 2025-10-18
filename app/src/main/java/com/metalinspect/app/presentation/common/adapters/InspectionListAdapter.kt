package com.metalinspect.app.presentation.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metalinspect.app.R
import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.databinding.ItemInspectionBinding
import com.metalinspect.app.utils.DateUtils

class InspectionListAdapter(
    private val onInspectionClick: (Inspection) -> Unit,
    private val onInspectionLongClick: (Inspection) -> Unit
) : ListAdapter<Inspection, InspectionListAdapter.InspectionViewHolder>(
    InspectionDiffCallback
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {
        val binding = ItemInspectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InspectionViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class InspectionViewHolder(
        private val binding: ItemInspectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onInspectionClick(getItem(position))
                }
            }
            
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onInspectionLongClick(getItem(position))
                    true
                } else {
                    false
                }
            }
        }
        
        fun bind(inspection: Inspection) {
            with(binding) {
                textViewLotNumber.text = inspection.lotNumber
                textViewPortLocation.text = inspection.portLocation
                textViewQuantity.text = "${inspection.quantity} ${inspection.unit}"
                textViewCreatedDate.text = DateUtils.formatDateTime(inspection.createdAt)
                
                // Status styling
                val statusColor = when (inspection.status) {
                    InspectionStatus.DRAFT -> ContextCompat.getColor(root.context, R.color.status_draft)
                    InspectionStatus.IN_PROGRESS -> ContextCompat.getColor(root.context, R.color.status_in_progress)
                    InspectionStatus.COMPLETED -> ContextCompat.getColor(root.context, R.color.status_completed)
                    InspectionStatus.CANCELLED -> ContextCompat.getColor(root.context, R.color.status_cancelled)
                }
                
                textViewStatus.text = when (inspection.status) {
                    InspectionStatus.DRAFT -> "Draft"
                    InspectionStatus.IN_PROGRESS -> "In Progress"
                    InspectionStatus.COMPLETED -> "Completed"
                    InspectionStatus.CANCELLED -> "Cancelled"
                }
                textViewStatus.setTextColor(statusColor)
                
                // Container number (optional)
                if (!inspection.containerNumber.isNullOrBlank()) {
                    textViewContainerNumber.text = "Container: ${inspection.containerNumber}"
                    textViewContainerNumber.visibility = android.view.View.VISIBLE
                } else {
                    textViewContainerNumber.visibility = android.view.View.GONE
                }
            }
        }
    }
    
    private object InspectionDiffCallback : DiffUtil.ItemCallback<Inspection>() {
        override fun areItemsTheSame(oldItem: Inspection, newItem: Inspection): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Inspection, newItem: Inspection): Boolean {
            return oldItem == newItem
        }
    }
}