package com.metalinspect.app.presentation.viewmodel

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.usecase.GetInspectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PdfExportViewModel @Inject constructor(
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState

    fun export(
        fileName: String,
        write: (out: java.io.OutputStream) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _exportState.value = ExportState.Running
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: throw IllegalStateException("Cannot create file")
                contentResolver.openOutputStream(uri)?.use { out -> write(out) }
                _exportState.value = ExportState.Success(uri = uri.toString())
            } catch (e: Exception) {
                _exportState.value = ExportState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class ExportState {
    object Idle : ExportState()
    object Running : ExportState()
    data class Success(val uri: String) : ExportState()
    data class Error(val message: String) : ExportState()
}
