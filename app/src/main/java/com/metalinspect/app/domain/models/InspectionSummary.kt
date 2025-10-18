package com.metalinspect.app.domain.models

import com.metalinspect.app.data.database.entities.*

data class InspectionSummary(
    val inspectionId: String,
    val lotNumber: String,
    val productTypeName: String,
    val inspectorName: String,
    val status: InspectionStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val completedAt: Long? = null,
    val photoCount: Int = 0,
    val defectCount: Int = 0,
    val criticalDefectCount: Int = 0
) {
    val isOverdue: Boolean get() {
        val currentTime = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        return status == InspectionStatus.IN_PROGRESS && (currentTime - createdAt) > oneDayMs
    }
    
    val hasIssues: Boolean get() = criticalDefectCount > 0 || isOverdue
}