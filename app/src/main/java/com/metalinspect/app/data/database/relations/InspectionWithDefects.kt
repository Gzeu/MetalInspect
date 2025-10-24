package com.metalinspect.app.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.metalinspect.app.data.database.entities.DefectEntity
import com.metalinspect.app.data.database.entities.InspectionEntity

/**
 * Room relation representing an inspection with its associated defects
 * Used for complex queries that need both inspection and defect data
 */
data class InspectionWithDefects(
    @Embedded 
    val inspection: InspectionEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "inspection_id"
    )
    val defects: List<DefectEntity> = emptyList()
) {
    /**
     * Computed properties for convenience
     */
    val totalDefects: Int
        get() = defects.size
        
    val criticalDefects: List<DefectEntity>
        get() = defects.filter { it.isCritical }
        
    val criticalDefectsCount: Int
        get() = criticalDefects.size
        
    val hasCriticalDefects: Boolean
        get() = criticalDefectsCount > 0
        
    val defectsByType: Map<String, List<DefectEntity>>
        get() = defects.groupBy { it.defectType }
        
    val defectsBySeverity: Map<String, List<DefectEntity>>
        get() = defects.groupBy { it.severity }
        
    val totalAffectedQuantity: Double
        get() = defects.mapNotNull { it.estimatedAffectedQuantity }.sum()
        
    val averageAffectedPercentage: Double
        get() = defects.mapNotNull { it.estimatedAffectedPercentage }
            .takeIf { it.isNotEmpty() }
            ?.average() ?: 0.0
}