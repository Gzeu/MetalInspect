package com.metalinspect.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalinspect.app.data.repository.InspectorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val inspectorRepository: InspectorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadActiveInspector()
    }

    private fun loadActiveInspector() {
        viewModelScope.launch {
            try {
                val activeInspector = inspectorRepository.getActiveInspector()
                _uiState.value = _uiState.value.copy(
                    activeInspectorName = activeInspector?.name
                )
            } catch (e: Exception) {
                // Handle error silently or show notification
            }
        }
    }

    fun exportData() {
        // Implementation for data export
        viewModelScope.launch {
            // TODO: Implement export functionality
        }
    }

    fun createBackup() {
        // Implementation for backup creation
        viewModelScope.launch {
            // TODO: Implement backup functionality
        }
    }
}

data class MainUiState(
    val activeInspectorName: String? = null,
    val isLoading: Boolean = false
)
