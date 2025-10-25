package com.metalinspect.app.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Industrial Dimensions for Field-Optimized UI
 * All touch targets optimized for gloved hand operation
 */
data class IndustrialDimensions(
    // Touch Targets - Minimum 48dp for accessibility and gloved hands
    val minTouchTarget: Dp = 48.dp,
    val buttonHeight: Dp = 56.dp,           // Large buttons for field use
    val smallButtonHeight: Dp = 48.dp,      // Minimum acceptable size
    val fabSize: Dp = 72.dp,                // Extra large FAB for camera
    
    // Spacing System
    val spaceXxs: Dp = 2.dp,
    val spaceXs: Dp = 4.dp,
    val spaceSm: Dp = 8.dp,
    val spaceMd: Dp = 12.dp,
    val spaceLg: Dp = 16.dp,
    val spaceXl: Dp = 24.dp,
    val spaceXxl: Dp = 32.dp,
    val spaceXxxl: Dp = 48.dp,
    
    // Card and Surface Dimensions
    val cardPadding: Dp = 16.dp,
    val cardRadius: Dp = 8.dp,
    val surfaceRadius: Dp = 12.dp,
    
    // Form Elements
    val textFieldHeight: Dp = 56.dp,        // Large text fields
    val textFieldPadding: Dp = 16.dp,
    
    // Status Indicators
    val statusIndicatorSize: Dp = 24.dp,
    val largeStatusIndicatorSize: Dp = 48.dp,
    
    // Progress Indicators
    val progressBarHeight: Dp = 8.dp,
    val circularProgressSize: Dp = 40.dp,
    
    // Navigation
    val bottomNavHeight: Dp = 80.dp,        // Extra height for field use
    val appBarHeight: Dp = 64.dp,           // Standard app bar
    
    // Content Spacing
    val listItemPadding: Dp = 16.dp,
    val sectionSpacing: Dp = 24.dp,
    val pageMargin: Dp = 16.dp,
    
    // Dividers and Borders
    val dividerThickness: Dp = 1.dp,
    val borderWidth: Dp = 1.dp,
    val focusBorderWidth: Dp = 2.dp,
    
    // Shadows and Elevation
    val cardElevation: Dp = 4.dp,
    val modalElevation: Dp = 8.dp,
    val tooltipElevation: Dp = 6.dp
)

// Composition Local for accessing dimensions
val LocalIndustrialDimensions = staticCompositionLocalOf { IndustrialDimensions() }