package com.metalinspect.app.presentation.theme

import androidx.compose.ui.graphics.Color

object IndustrialColors {
    val SafetyOrange = Color(0xFFFF6B00)
    val IndustrialBlue = Color(0xFF1565C0)
    val MetalGray = Color(0xFF37474F)
    val QualityGreen = Color(0xFF2E7D32)
    val DefectRed = Color(0xFFD32F2F)
    val NeutralSilver = Color(0xFF90A4AE)

    val WarningAmber = Color(0xFFF57C00)
    val InProgressBlue = Color(0xFF1976D2)
    val PendingGray = Color(0xFF616161)

    val SurfaceLight = Color(0xFFF5F5F5)
    val SurfaceDark = Color(0xFF121212)
    val SurfaceVariant = Color(0xFF263238)

    val OnSurfaceLight = Color(0xFF212121)
    val OnSurfaceDark = Color(0xFFE0E0E0)
    val OnPrimary = Color(0xFFFFFFFF)

    fun getStatusColor(status: String): Color = when (status.lowercase()) {
        "completed","approved" -> QualityGreen
        "in_progress","active" -> InProgressBlue
        "pending","draft" -> PendingGray
        "failed","rejected" -> DefectRed
        "warning" -> WarningAmber
        else -> NeutralSilver
    }

    fun getSeverityColor(severity: String): Color = when (severity.lowercase()) {
        "critical","high" -> DefectRed
        "medium","moderate" -> WarningAmber
        "low","minor" -> QualityGreen
        else -> NeutralSilver
    }
}