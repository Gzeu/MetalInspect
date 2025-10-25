package com.metalinspect.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.metalinspect.app.presentation.theme.IndustrialColors

@Composable
fun StatusIndicator(
    status: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    showLabel: Boolean = false,
    includeIcon: Boolean = true
) {
    val statusColor = IndustrialColors.getStatusColor(status)
    val statusIcon = when (status.lowercase()) {
        "completed","approved" -> Icons.Default.CheckCircle
        "failed","rejected","error" -> Icons.Default.Error
        "warning" -> Icons.Default.Warning
        "in_progress","active","processing" -> Icons.Default.Autorenew
        "pending","draft" -> Icons.Default.Schedule
        "paused" -> Icons.Default.Pause
        else -> Icons.Default.Circle
    }

    if (showLabel) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            StatusDot(color = statusColor, icon = if (includeIcon) statusIcon else null, size = size)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = status.replace("_"," ").uppercase(), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), color = statusColor)
        }
    } else {
        StatusDot(color = statusColor, icon = if (includeIcon) statusIcon else null, size = size, modifier = modifier)
    }
}

@Composable
private fun StatusDot(color: Color, icon: ImageVector?, size: Dp, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(size).clip(CircleShape).then(Modifier), contentAlignment = Alignment.Center) {
        Surface(color = color, shape = CircleShape) { Box(modifier = Modifier.size(size)) }
        if (icon != null) Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(size * 0.6f))
    }
}
