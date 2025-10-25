package com.metalinspect.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightIndustrial = lightColorScheme(
    primary = IndustrialColors.IndustrialBlue,
    onPrimary = IndustrialColors.OnPrimary,
    secondary = IndustrialColors.SafetyOrange,
    onSecondary = IndustrialColors.OnPrimary,
    tertiary = IndustrialColors.QualityGreen,
    error = IndustrialColors.DefectRed,
    background = IndustrialColors.SurfaceLight,
    onBackground = IndustrialColors.OnSurfaceLight,
    surface = Color.White,
    onSurface = IndustrialColors.OnSurfaceLight,
    surfaceVariant = IndustrialColors.SurfaceLight
)

private val DarkIndustrial = darkColorScheme(
    primary = IndustrialColors.SafetyOrange,
    onPrimary = Color.Black,
    secondary = IndustrialColors.IndustrialBlue,
    onSecondary = IndustrialColors.OnPrimary,
    tertiary = IndustrialColors.QualityGreen,
    error = IndustrialColors.DefectRed,
    background = IndustrialColors.SurfaceDark,
    onBackground = IndustrialColors.OnSurfaceDark,
    surface = IndustrialColors.SurfaceVariant,
    onSurface = IndustrialColors.OnSurfaceDark,
    surfaceVariant = IndustrialColors.MetalGray
)

@Composable
fun IndustrialTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val scheme = if (darkTheme) DarkIndustrial else LightIndustrial
    MaterialTheme(
        colorScheme = scheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes
    ) {
        CompositionLocalProvider(content = content)
    }
}