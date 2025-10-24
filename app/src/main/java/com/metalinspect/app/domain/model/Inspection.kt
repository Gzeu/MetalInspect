package com.metalinspect.app.domain.model

import java.util.Date

/**
 * Domain model representing an inspection
 */
data class Inspection(
    val id: String,
    val vesselName: String,
    val cargoDescription: String,
    val inspectionDate: Date,
    val inspectorName: String,
    val location: String,
    val weatherConditions: String,
    val cargoQuantity: Double,
    val cargoUnit: String,
    val inspectionType: String,
    val qualityGrade: String?,
    val moistureContent: Double?,
    val contaminationLevel: String?,
    val overallCondition: String,
    val notes: String?,
    val photoPaths: List<String> = emptyList(),
    val isCompleted: Boolean = false,
    val isSynced: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)