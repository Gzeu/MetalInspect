package com.metalinspect.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * Industrial Theme for MetalInspect
 * Optimized for field use in manufacturing environments
 */

// Light Theme Colors
private val LightIndustrialColorScheme = lightColorScheme(
    primary = IndustrialColors.IndustrialBlue,
    onPrimary = IndustrialColors.OnPrimary,
    primaryContainer = IndustrialColors.IndustrialBlue.copy(alpha = 0.1f),
    onPrimaryContainer = IndustrialColors.IndustrialBlue,
    
    secondary = IndustrialColors.SafetyOrange,
    onSecondary = IndustrialColors.OnPrimary,
    secondaryContainer = IndustrialColors.SafetyOrange.copy(alpha = 0.1f),
    onSecondaryContainer = IndustrialColors.SafetyOrange,
    
    tertiary = IndustrialColors.QualityGreen,
    onTertiary = IndustrialColors.OnPrimary,
    
    error = IndustrialColors.DefectRed,
    onError = IndustrialColors.OnPrimary,
    errorContainer = IndustrialColors.DefectRed.copy(alpha = 0.1f),
    onErrorContainer = IndustrialColors.DefectRed,
    
    background = IndustrialColors.SurfaceLight,
    onBackground = IndustrialColors.OnSurfaceLight,
    surface = Color.White,
    onSurface = IndustrialColors.OnSurfaceLight,
    surfaceVariant = IndustrialColors.SurfaceLight,
    onSurfaceVariant = IndustrialColors.NeutralSilver,
    
    outline = IndustrialColors.NeutralSilver,
    outlineVariant = IndustrialColors.Divider
)

// Dark Theme Colors - Optimized for industrial environments
private val DarkIndustrialColorScheme = darkColorScheme(
    primary = IndustrialColors.SafetyOrange,
    onPrimary = Color.Black,
    primaryContainer = IndustrialColors.SafetyOrange.copy(alpha = 0.2f),
    onPrimaryContainer = IndustrialColors.SafetyOrange,
    
    secondary = IndustrialColors.IndustrialBlue,
    onSecondary = IndustrialColors.OnPrimary,
    secondaryContainer = IndustrialColors.IndustrialBlue.copy(alpha = 0.2f),
    onSecondaryContainer = IndustrialColors.IndustrialBlue,
    
    tertiary = IndustrialColors.QualityGreen,
    onTertiary = Color.Black,
    
    error = IndustrialColors.DefectRed,
    onError = IndustrialColors.OnPrimary,
    errorContainer = IndustrialColors.DefectRed.copy(alpha = 0.2f),
    onErrorContainer = IndustrialColors.DefectRed,
    
    background = IndustrialColors.SurfaceDark,
    onBackground = IndustrialColors.OnSurfaceDark,
    surface = IndustrialColors.SurfaceVariant,
    onSurface = IndustrialColors.OnSurfaceDark,
    surfaceVariant = IndustrialColors.MetalGray,
    onSurfaceVariant = IndustrialColors.NeutralSilver,
    
    outline = IndustrialColors.NeutralSilver,
    outlineVariant = IndustrialColors.Divider
)

@Composable
fun IndustrialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkIndustrialColorScheme
    } else {
        LightIndustrialColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = IndustrialTypography,
        shapes = IndustrialShapes,
        content = {
            CompositionLocalProvider(
                LocalIndustrialDimensions provides IndustrialDimensions(),
                content = content
            )
        }
    )
}