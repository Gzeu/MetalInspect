package com.metalinspect.app.presentation.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metalinspect.app.R
import com.metalinspect.app.data.database.entities.DefectRecord
import com.metalinspect.app.data.database.entities.DefectSeverity
import com.metalinspect.app.databinding.ItemDefectBinding
import com.metalinspect.app.utils.DateUtils

class DefectListAdapter(
    private val onDefectClick: (DefectRecord) -> Unit,
    private val onDefectLongClick: (DefectRecord) -> Unit
) : ListAdapter<DefectRecord, DefectListAdapter.DefectViewHolder>(
    DefectDiffCallback
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefectViewHolder {
        val binding = ItemDefectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DefectViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: DefectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class DefectViewHolder(
        private val binding: ItemDefectBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDefectClick(getItem(position))
                }
            }
            
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDefectLongClick(getItem(position))
                    true
                } else {
                    false
                }
            }
        }
        
        fun bind(defect: DefectRecord) {
            with(binding) {
                textViewDefectType.text = defect.defectType
                textViewCategory.text = defect.defectCategory.name.lowercase().replaceFirstChar { it.uppercase() }
                textViewDescription.text = defect.description
                textViewCount.text = "Count: ${defect.count}"
                textViewCreatedDate.text = DateUtils.formatDateTime(defect.createdAt)
                
                // Severity styling
                val severityColor = when (defect.severity) {
                    DefectSeverity.CRITICAL -> ContextCompat.getColor(root.context, R.color.severity_critical)
                    DefectSeverity.MAJOR -> ContextCompat.getColor(root.context, R.color.severity_major)
                    DefectSeverity.MINOR -> ContextCompat.getColor(root.context, R.color.severity_minor)
                    DefectSeverity.COSMETIC -> ContextCompat.getColor(root.context, R.color.severity_cosmetic)
                }
                
                textViewSeverity.text = defect.severity.name.lowercase().replaceFirstChar { it.uppercase() }
                textViewSeverity.setTextColor(severityColor)
                
                // Background color based on severity
                val backgroundColor = when (defect.severity) {
                    DefectSeverity.CRITICAL -> ContextCompat.getColor(root.context, R.color.error_background)
                    DefectSeverity.MAJOR -> ContextCompat.getColor(root.context, R.color.warning_background)
                    else -> ContextCompat.getColor(root.context, android.R.color.transparent)
                }
                root.setBackgroundColor(backgroundColor)
                
                // Location notes (optional)
                if (!defect.locationNotes.isNullOrBlank()) {
                    textViewLocation.text = "Location: ${defect.locationNotes}"
                    textViewLocation.visibility = android.view.View.VISIBLE
                } else {
                    textViewLocation.visibility = android.view.View.GONE
                }
            }
        }
    }
    
    private object DefectDiffCallback : DiffUtil.ItemCallback<DefectRecord>() {
        override fun areItemsTheSame(oldItem: DefectRecord, newItem: DefectRecord): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: DefectRecord, newItem: DefectRecord): Boolean {
            return oldItem == newItem
        }
    }
}