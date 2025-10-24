package com.metalinspect.app.domain.model

import java.util.Date

/**
 * Domain model representing a defect found during inspection
 */
data class Defect(
    val id: String,
    val inspectionId: String,
    val defectType: String,
    val severity: String,
    val description: String,
    val locationDescription: String?,
    val estimatedAffectedQuantity: Double?,
    val estimatedAffectedPercentage: Double?,
    val photoPaths: List<String> = emptyList(),
    val recommendedAction: String?,
    val isCritical: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)