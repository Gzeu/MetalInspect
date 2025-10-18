package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "defect_records",
    indices = [
        Index(value = ["inspection_id"]),
        Index(value = ["defect_type"]),
        Index(value = ["severity"])
    ]
)
@Parcelize
data class DefectRecord(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "inspection_id")
    val inspectionId: String,
    
    @ColumnInfo(name = "defect_type")
    val defectType: String,
    
    @ColumnInfo(name = "defect_category")
    val defectCategory: DefectCategory,
    
    @ColumnInfo(name = "severity")
    val severity: DefectSeverity,
    
    @ColumnInfo(name = "count")
    val count: Int = 1,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "location_notes")
    val locationNotes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

enum class DefectCategory {
    SURFACE,
    DIMENSIONAL,
    MATERIAL,
    PACKAGING,
    DOCUMENTATION
}

enum class DefectSeverity {
    CRITICAL,
    MAJOR,
    MINOR,
    COSMETIC
}
