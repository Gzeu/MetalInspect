package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "inspection_photos",
    indices = [
        Index(value = ["inspection_id"]),
        Index(value = ["defect_record_id"]),
        Index(value = ["created_at"])
    ]
)
@Parcelize
data class InspectionPhoto(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "inspection_id")
    val inspectionId: String,
    
    @ColumnInfo(name = "defect_record_id")
    val defectRecordId: String? = null,
    
    @ColumnInfo(name = "file_path")
    val filePath: String,
    
    @ColumnInfo(name = "caption")
    val caption: String? = null,
    
    @ColumnInfo(name = "sequence_index")
    val sequenceIndex: Int = 0,
    
    @ColumnInfo(name = "file_size")
    val fileSize: Long,
    
    @ColumnInfo(name = "image_width")
    val imageWidth: Int,
    
    @ColumnInfo(name = "image_height")
    val imageHeight: Int,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
