package com.metalinspect.app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metalinspect.app.presentation.components.IndustrialButton
import com.metalinspect.app.presentation.components.InspectionStatusCard

@Composable
fun InspectionListScreen(
    onAddInspection: () -> Unit = {},
    onOpenInspection: (String) -> Unit = {}
) {
    val items = remember {
        listOf(
            DemoInspection("INS-001","PROD-4567","in_progress",0.35f),
            DemoInspection("INS-002","PROD-9876","completed",1.0f),
            DemoInspection("INS-003","PROD-2222","pending",0.0f)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddInspection) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Inspections", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            IndustrialButton(onClick = onAddInspection, text = "New Inspection")
            Spacer(Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    InspectionStatusCard(
                        inspectionId = item.id,
                        productId = item.productId,
                        status = item.status,
                        progress = item.progress,
                        onClick = { onOpenInspection(item.id) }
                    )
                }
            }
        }
    }
}

data class DemoInspection(val id: String, val productId: String, val status: String, val progress: Float)
