package com.metalinspect.app.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

/**
 * Room entity representing a defect found during inspection
 * Maps to 'defects' table with foreign key relationship to inspections
 */
@Entity(
    tableName = "defects",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["inspection_id"])]
)
@TypeConverters(DateConverter::class)
data class DefectEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "inspection_id")
    val inspectionId: String,
    
    @ColumnInfo(name = "defect_type")
    val defectType: String, // "rust", "contamination", "damage", "moisture", "other"
    
    @ColumnInfo(name = "severity")
    val severity: String, // "low", "medium", "high", "critical"
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "location_description")
    val locationDescription: String?,
    
    @ColumnInfo(name = "estimated_affected_quantity")
    val estimatedAffectedQuantity: Double?,
    
    @ColumnInfo(name = "estimated_affected_percentage")
    val estimatedAffectedPercentage: Double?,
    
    @ColumnInfo(name = "photo_paths")
    val photoPaths: List<String> = emptyList(),
    
    @ColumnInfo(name = "recommended_action")
    val recommendedAction: String?,
    
    @ColumnInfo(name = "is_critical")
    val isCritical: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)