package com.metalinspect.app.presentation.inspection.detail

import androidx.lifecycle.viewModelScope
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.domain.usecases.inspection.GetInspectionUseCase
import com.metalinspect.app.domain.usecases.inspection.UpdateInspectionUseCase
import com.metalinspect.app.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionDetailViewModel @Inject constructor(
    private val getInspectionUseCase: GetInspectionUseCase,
    private val updateInspectionUseCase: UpdateInspectionUseCase
) : BaseViewModel() {
    
    private val _inspectionDetails = MutableStateFlow<InspectionWithDetails?>(null)
    val inspectionDetails: StateFlow<InspectionWithDetails?> = _inspectionDetails.asStateFlow()
    
    fun loadInspectionDetails(inspectionId: String) {
        viewModelScope.launch {
            safeCall(
                call = {
                    getInspectionUseCase.getInspectionWithDetails(inspectionId)
                },
                onSuccess = { details ->
                    _inspectionDetails.value = details
                },
                onError = { error ->
                    showError("Failed to load inspection details: ${error.message}")
                }
            )
        }
    }
    
    fun completeInspection(inspectionId: String) {
        viewModelScope.launch {
            safeCall(
                call = {
                    updateInspectionUseCase.completeInspection(inspectionId)
                },
                onSuccess = { result ->
                    result.fold(
                        onSuccess = {
                            showMessage("Inspection completed successfully")
                            loadInspectionDetails(inspectionId) // Refresh
                        },
                        onFailure = { error ->
                            showError(error.message ?: "Failed to complete inspection")
                        }
                    )
                },
                onError = { error ->
                    showError("Failed to complete inspection: ${error.message}")
                }
            )
        }
    }
    
    fun showAddDefectDialog() {
        // TODO: Implement add defect dialog
        showMessage("Add defect dialog - TODO")
    }
}