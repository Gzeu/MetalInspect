package com.metalinspect.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metalinspect.app.presentation.theme.IndustrialColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionStatusCard(
    inspectionId: String,
    productId: String,
    status: String,
    progress: Float = 0f,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val statusColor = IndustrialColors.getStatusColor(status)
    val statusIcon = when (status.lowercase()) {
        "completed","approved" -> Icons.Default.CheckCircle
        "failed","rejected" -> Icons.Default.Error
        else -> Icons.Default.Schedule
    }
    Card(onClick = onClick, modifier = modifier.fillMaxWidth().height(120.dp)) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 6.dp.toPx()
                    val d = size.minDimension
                    val topLeft = Offset((size.width - d)/2f, (size.height - d)/2f)
                    val ringSize = Size(d, d)
                    drawArc(color = statusColor.copy(alpha = 0.2f), startAngle = -90f, sweepAngle = 360f, useCenter = false, topLeft = topLeft, size = ringSize, style = Stroke(width = stroke, cap = StrokeCap.Round))
                    if (progress > 0f) drawArc(color = statusColor, startAngle = -90f, sweepAngle = 360f*progress.coerceIn(0f,1f), useCenter = false, topLeft = topLeft, size = ringSize, style = Stroke(width = stroke, cap = StrokeCap.Round))
                }
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(statusColor), contentAlignment = Alignment.Center) {
                    Icon(statusIcon, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(productId, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(Modifier.height(4.dp))
                Text("ID: $inspectionId", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Surface(color = statusColor.copy(alpha=0.1f), shape = MaterialTheme.shapes.small) {
                    Text(status.uppercase(), modifier = Modifier.padding(horizontal=8.dp, vertical=4.dp), color = statusColor, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}