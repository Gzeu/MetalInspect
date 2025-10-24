package com.metalinspect.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.usecase.CreateInspectionUseCase
import com.metalinspect.app.domain.usecase.DeleteInspectionUseCase
import com.metalinspect.app.domain.usecase.GetInspectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for inspection list screen
 * Manages inspection list state and user interactions
 */
@HiltViewModel
class InspectionListViewModel @Inject constructor(
    private val getInspectionsUseCase: GetInspectionsUseCase,
    private val createInspectionUseCase: CreateInspectionUseCase,
    private val deleteInspectionUseCase: DeleteInspectionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InspectionListUiState())
    val uiState: StateFlow<InspectionListUiState> = _uiState.asStateFlow()
    
    init {
        loadInspections()
    }
    
    /**
     * Load inspections from repository
     */
    private fun loadInspections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getInspectionsUseCase()
                .catch { exception ->
                    Timber.e(exception, "Failed to load inspections")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load inspections: ${exception.message}"
                    )
                }
                .collect { inspections ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        inspections = inspections,
                        error = null
                    )
                }
        }
    }
    
    /**
     * Create a new inspection
     */
    fun createInspection(inspection: Inspection) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val result = createInspectionUseCase(inspection)
                
                result.fold(
                    onSuccess = { inspectionId ->
                        Timber.d("Inspection created successfully: $inspectionId")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                        // Inspections will be updated automatically via Flow
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "Failed to create inspection")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to create inspection: ${exception.message}"
                        )
                    }
                )
            } catch (exception: Exception) {
                Timber.e(exception, "Unexpected error creating inspection")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Unexpected error: ${exception.message}"
                )
            }
        }
    }
    
    /**
     * Delete an inspection
     */
    fun deleteInspection(inspectionId: String) {
        viewModelScope.launch {
            try {
                val result = deleteInspectionUseCase(inspectionId)
                
                result.fold(
                    onSuccess = {
                        Timber.d("Inspection deleted successfully: $inspectionId")
                        // Inspections will be updated automatically via Flow
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "Failed to delete inspection")
                        _uiState.value = _uiState.value.copy(
                            error = "Failed to delete inspection: ${exception.message}"
                        )
                    }
                )
            } catch (exception: Exception) {
                Timber.e(exception, "Unexpected error deleting inspection")
                _uiState.value = _uiState.value.copy(
                    error = "Unexpected error: ${exception.message}"
                )
            }
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Refresh inspections
     */
    fun refresh() {
        loadInspections()
    }
}

/**
 * UI state for inspection list screen
 */
data class InspectionListUiState(
    val isLoading: Boolean = false,
    val inspections: List<Inspection> = emptyList(),
    val error: String? = null
)