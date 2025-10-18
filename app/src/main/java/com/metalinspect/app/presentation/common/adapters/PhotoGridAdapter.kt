package com.metalinspect.app.presentation.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metalinspect.app.R
import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.databinding.ItemPhotoBinding
import com.metalinspect.app.utils.DateUtils

class PhotoGridAdapter(
    private val onPhotoClick: (InspectionPhoto) -> Unit,
    private val onPhotoLongClick: (InspectionPhoto) -> Unit
) : ListAdapter<InspectionPhoto, PhotoGridAdapter.PhotoViewHolder>(
    PhotoDiffCallback
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPhotoClick(getItem(position))
                }
            }
            
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPhotoLongClick(getItem(position))
                    true
                } else {
                    false
                }
            }
        }
        
        fun bind(photo: InspectionPhoto) {
            with(binding) {
                // Load photo with Glide
                Glide.with(imageViewPhoto.context)
                    .load(photo.filePath)
                    .placeholder(R.drawable.ic_camera)
                    .error(R.drawable.ic_report)
                    .centerCrop()
                    .into(imageViewPhoto)
                
                // Photo info
                textViewSequence.text = "#${photo.sequenceIndex + 1}"
                textViewTimestamp.text = DateUtils.formatTime(photo.createdAt)
                
                // Caption (optional)
                if (!photo.caption.isNullOrBlank()) {
                    textViewCaption.text = photo.caption
                    textViewCaption.visibility = android.view.View.VISIBLE
                } else {
                    textViewCaption.visibility = android.view.View.GONE
                }
                
                // Defect indicator
                if (photo.defectRecordId != null) {
                    iconDefectIndicator.visibility = android.view.View.VISIBLE
                } else {
                    iconDefectIndicator.visibility = android.view.View.GONE
                }
            }
        }
    }
    
    private object PhotoDiffCallback : DiffUtil.ItemCallback<InspectionPhoto>() {
        override fun areItemsTheSame(oldItem: InspectionPhoto, newItem: InspectionPhoto): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: InspectionPhoto, newItem: InspectionPhoto): Boolean {
            return oldItem == newItem
        }
    }
}