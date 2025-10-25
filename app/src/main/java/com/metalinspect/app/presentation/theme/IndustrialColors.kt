package com.metalinspect.app.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Industrial Design System Colors
 * Optimized for manufacturing and quality control environments
 */
object IndustrialColors {
    // Primary Industrial Colors
    val SafetyOrange = Color(0xFFFF6B00)      // Warnings, alerts, critical actions
    val IndustrialBlue = Color(0xFF1565C0)    // Primary actions, navigation
    val MetalGray = Color(0xFF37474F)         // Backgrounds, cards
    val QualityGreen = Color(0xFF2E7D32)      // Success, approved status
    val DefectRed = Color(0xFFD32F2F)         // Errors, defects, failures
    val NeutralSilver = Color(0xFF90A4AE)     // Secondary text, disabled states
    
    // Extended Palette for Status Indicators
    val WarningAmber = Color(0xFFF57C00)      // Warning states
    val InProgressBlue = Color(0xFF1976D2)    // In-progress status
    val PendingGray = Color(0xFF616161)       // Pending, draft states
    
    // Surface Colors for Industrial Theme
    val SurfaceLight = Color(0xFFF5F5F5)      // Light backgrounds
    val SurfaceDark = Color(0xFF121212)       // Dark backgrounds
    val SurfaceVariant = Color(0xFF263238)    // Card backgrounds in dark
    
    // Text Colors
    val OnSurfaceLight = Color(0xFF212121)    // Text on light surfaces
    val OnSurfaceDark = Color(0xFFE0E0E0)     // Text on dark surfaces
    val OnPrimary = Color(0xFFFFFFFF)         // Text on primary colors
    
    // Utility Colors
    val Divider = Color(0x1F000000)           // Dividers and borders
    val Shadow = Color(0x33000000)            // Drop shadows
    val Overlay = Color(0x80000000)           // Modal overlays
    
    /**
     * Get status color based on inspection status
     */
    fun getStatusColor(status: String): Color {
        return when (status.lowercase()) {
            "completed", "approved" -> QualityGreen
            "in_progress", "active" -> InProgressBlue
            "pending", "draft" -> PendingGray
            "failed", "rejected" -> DefectRed
            "warning" -> WarningAmber
            else -> NeutralSilver
        }
    }
    
    /**
     * Get severity color for defects
     */
    fun getSeverityColor(severity: String): Color {
        return when (severity.lowercase()) {
            "critical", "high" -> DefectRed
            "medium", "moderate" -> WarningAmber
            "low", "minor" -> QualityGreen
            else -> NeutralSilver
        }
    }
}