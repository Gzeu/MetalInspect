package com.metalinspect.app.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.metalinspect.app.presentation.components.DefectSeveritySelector
import com.metalinspect.app.presentation.components.IndustrialButton
import com.metalinspect.app.presentation.components.IndustrialTextField

@Composable
fun InspectionEditorScreen(
    onSave: (InspectionForm) -> Unit,
    onCancel: () -> Unit,
    initial: InspectionForm = InspectionForm()
) {
    var productId by remember { mutableStateOf(initial.productId) }
    var inspector by remember { mutableStateOf(initial.inspector) }
    var severity by remember { mutableStateOf(initial.severity) }
    var notes by remember { mutableStateOf(initial.notes) }

    Scaffold(topBar = { SmallTopAppBar(title = { Text("New Inspection") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IndustrialTextField(
                value = productId,
                onValueChange = { productId = it },
                label = "Product ID",
                placeholder = "e.g. PROD-1234"
            )
            IndustrialTextField(
                value = inspector,
                onValueChange = { inspector = it },
                label = "Inspector",
                placeholder = "Full name"
            )
            DefectSeveritySelector(
                selectedSeverity = severity,
                onSeverityChange = { severity = it }
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.heightIn(min = 120.dp).fillMaxSize(fraction = 0.0f),
                singleLine = false,
                maxLines = 6,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IndustrialButton(onClick = { onSave(InspectionForm(productId, inspector, severity, notes)) }, text = "Save", icon = Icons.Default.Check)
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
            }
        }
    }
}

data class InspectionForm(
    val productId: String = "",
    val inspector: String = "",
    val severity: String = "LOW",
    val notes: String = ""
)
