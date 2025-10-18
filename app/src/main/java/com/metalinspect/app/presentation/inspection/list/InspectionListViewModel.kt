package com.metalinspect.app.presentation.inspection.list

import androidx.lifecycle.viewModelScope
import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.domain.usecases.inspection.GetInspectionUseCase
import com.metalinspect.app.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionListViewModel @Inject constructor(
    private val getInspectionUseCase: GetInspectionUseCase
) : BaseViewModel() {
    
    private val _inspections = MutableStateFlow<List<Inspection>>(emptyList())
    val inspections: StateFlow<List<Inspection>> = _inspections.asStateFlow()
    
    init {
        loadInspections()
    }
    
    private fun loadInspections() {
        viewModelScope.launch {
            safeCall(
                call = {
                    getInspectionUseCase.getAllInspections()
                },
                onSuccess = { inspectionsFlow ->
                    inspectionsFlow.collect { inspectionList ->
                        _inspections.value = inspectionList
                    }
                },
                onError = { error ->
                    showError("Failed to load inspections: ${error.message}")
                }
            )
        }
    }
    
    fun showInspectionOptions(inspection: Inspection) {
        // Show options dialog or bottom sheet
        showMessage("Options for ${inspection.lotNumber}")
    }
    
    fun refreshInspections() {
        loadInspections()
    }
}