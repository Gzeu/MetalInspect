package com.metalinspect.app.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Industrial Shape System
 * Conservative rounded corners for professional appearance
 */
val IndustrialShapes = Shapes(
    // Small components - buttons, chips, badges
    small = RoundedCornerShape(8.dp),
    
    // Medium components - cards, text fields
    medium = RoundedCornerShape(12.dp),
    
    // Large components - modals, sheets
    large = RoundedCornerShape(16.dp)
)

// Additional shape definitions for specific use cases
object IndustrialShapeTokens {
    val ButtonShape = RoundedCornerShape(8.dp)
    val CardShape = RoundedCornerShape(12.dp)
    val TextFieldShape = RoundedCornerShape(8.dp)
    val ModalShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val StatusBadgeShape = RoundedCornerShape(16.dp)
    val ImageShape = RoundedCornerShape(8.dp)
}