package com.metalinspect.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InspectionDetailViewModel @Inject constructor(
    private val repository: InspectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionDetailUiState())
    val uiState: StateFlow<InspectionDetailUiState> = _uiState.asStateFlow()

    fun load(inspectionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = repository.getInspectionWithDefects(inspectionId)
                result.fold(
                    onSuccess = { pair ->
                        if (pair == null) {
                            _uiState.value = _uiState.value.copy(isLoading = false, error = "Not found")
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                inspection = pair.first,
                                defects = pair.second,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        Timber.e(e)
                        _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                    }
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

data class InspectionDetailUiState(
    val isLoading: Boolean = false,
    val inspection: Inspection? = null,
    val defects: List<Defect> = emptyList(),
    val error: String? = null
)