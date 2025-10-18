package com.metalinspect.app.domain.models

import com.metalinspect.app.data.database.entities.*

data class InspectionWithDetails(
    val inspection: Inspection,
    val inspector: Inspector,
    val productType: ProductType,
    val defects: List<DefectRecord> = emptyList(),
    val photos: List<InspectionPhoto> = emptyList(),
    val checklistResponses: List<ChecklistResponse> = emptyList()
) {
    val totalDefects: Int get() = defects.size
    val criticalDefects: Int get() = defects.count { it.severity == DefectSeverity.CRITICAL }
    val majorDefects: Int get() = defects.count { it.severity == DefectSeverity.MAJOR }
    val minorDefects: Int get() = defects.count { it.severity == DefectSeverity.MINOR }
    val cosmeticDefects: Int get() = defects.count { it.severity == DefectSeverity.COSMETIC }
    
    val photoCount: Int get() = photos.size
    val completedChecklistItems: Int get() = checklistResponses.size
    
    val hasDefects: Boolean get() = defects.isNotEmpty()
    val hasCriticalDefects: Boolean get() = criticalDefects > 0
    val hasPhotos: Boolean get() = photos.isNotEmpty()
    
    val isComplete: Boolean get() = inspection.status == InspectionStatus.COMPLETED
    val isDraft: Boolean get() = inspection.status == InspectionStatus.DRAFT
    val isInProgress: Boolean get() = inspection.status == InspectionStatus.IN_PROGRESS
    val isCancelled: Boolean get() = inspection.status == InspectionStatus.CANCELLED
}