package com.metalinspect.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metalinspect.app.presentation.components.InspectionStatusCard
import com.metalinspect.app.presentation.theme.IndustrialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndustrialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DemoScreen()
                }
            }
        }
    }
}

@Composable
private fun DemoScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("MetalInspect - Industrial MVP", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        InspectionStatusCard(
            inspectionId = "INS-001",
            productId = "PROD-4567",
            status = "in_progress",
            progress = 0.35f
        )
    }
}