package com.metalinspect.app.presentation.inspection.create

import androidx.lifecycle.viewModelScope
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.domain.usecases.inspection.CreateInspectionUseCase
import com.metalinspect.app.data.repository.InspectorRepository
import com.metalinspect.app.data.repository.ProductRepository
import com.metalinspect.app.domain.validators.FormValidator
import com.metalinspect.app.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateInspectionViewModel @Inject constructor(
    private val createInspectionUseCase: CreateInspectionUseCase,
    private val inspectorRepository: InspectorRepository,
    private val productRepository: ProductRepository,
    private val formValidator: FormValidator
) : BaseViewModel() {
    
    private val _formState = MutableStateFlow(CreateInspectionFormState())
    val formState: StateFlow<CreateInspectionFormState> = _formState.asStateFlow()
    
    private val _productTypes = MutableStateFlow<List<ProductType>>(emptyList())
    val productTypes: StateFlow<List<ProductType>> = _productTypes.asStateFlow()
    
    private val _inspectors = MutableStateFlow<List<Inspector>>(emptyList())
    val inspectors: StateFlow<List<Inspector>> = _inspectors.asStateFlow()
    
    private val _saveResult = MutableStateFlow<CreateInspectionResult?>(null)
    val saveResult: StateFlow<CreateInspectionResult?> = _saveResult.asStateFlow()
    
    fun loadProductTypes() {
        viewModelScope.launch {
            productRepository.getActiveProductTypes().collect { types ->
                _productTypes.value = types
            }
        }
    }
    
    fun loadInspectors() {
        viewModelScope.launch {
            inspectorRepository.getActiveInspectors().collect { inspectors ->
                _inspectors.value = inspectors
            }
        }
    }
    
    fun validateLotNumber(lotNumber: String) {
        val validation = formValidator.validateCreateInspectionForm(
            lotNumber = lotNumber,
            containerNumber = null,
            quantity = null,
            weight = null,
            unit = null,
            portLocation = null,
            weatherConditions = null,
            productTypeId = null,
            inspectorId = null,
            notes = null
        )
        
        _formState.value = _formState.value.copy(
            lotNumberError = validation["lotNumber"]?.errorMessage
        )
    }
    
    fun validateQuantity(quantity: String) {
        val validation = formValidator.validateCreateInspectionForm(
            lotNumber = null,
            containerNumber = null,
            quantity = quantity,
            weight = null,
            unit = null,
            portLocation = null,
            weatherConditions = null,
            productTypeId = null,
            inspectorId = null,
            notes = null
        )
        
        _formState.value = _formState.value.copy(
            quantityError = validation["quantity"]?.errorMessage
        )
    }
    
    fun validateWeight(weight: String) {
        val validation = formValidator.validateCreateInspectionForm(
            lotNumber = null,
            containerNumber = null,
            quantity = null,
            weight = weight,
            unit = null,
            portLocation = null,
            weatherConditions = null,
            productTypeId = null,
            inspectorId = null,
            notes = null
        )
        
        _formState.value = _formState.value.copy(
            weightError = validation["weight"]?.errorMessage
        )
    }
    
    fun saveInspection(formData: CreateInspectionFormData, isDraft: Boolean) {
        viewModelScope.launch {
            safeCall(
                call = {
                    // Validate entire form
                    val validationResults = formValidator.validateCreateInspectionForm(
                        lotNumber = formData.lotNumber,
                        containerNumber = formData.containerNumber,
                        quantity = formData.quantity.toString(),
                        weight = formData.weight.toString(),
                        unit = formData.unit,
                        portLocation = formData.portLocation,
                        weatherConditions = formData.weatherConditions,
                        productTypeId = formData.productTypeId,
                        inspectorId = formData.inspectorId,
                        notes = formData.notes
                    )
                    
                    if (!formValidator.isFormValid(validationResults)) {
                        throw Exception(formValidator.getFirstError(validationResults) ?: "Form validation failed")
                    }
                    
                    // Create inspection entity
                    val inspection = Inspection(
                        id = "", // Will be generated by use case
                        lotNumber = formData.lotNumber,
                        containerNumber = formData.containerNumber,
                        productTypeId = formData.productTypeId,
                        quantity = formData.quantity,
                        weight = formData.weight,
                        unit = formData.unit,
                        portLocation = formData.portLocation,
                        weatherConditions = formData.weatherConditions,
                        inspectorId = formData.inspectorId,
                        status = if (isDraft) InspectionStatus.DRAFT else InspectionStatus.IN_PROGRESS,
                        notes = formData.notes,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    createInspectionUseCase(inspection)
                },
                onSuccess = { result ->
                    result.fold(
                        onSuccess = { inspectionId ->
                            _saveResult.value = CreateInspectionResult.Success(inspectionId, isDraft)
                        },
                        onFailure = { error ->
                            _saveResult.value = CreateInspectionResult.Error(error.message ?: "Unknown error")
                        }
                    )
                },
                onError = { error ->
                    _saveResult.value = CreateInspectionResult.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
    
    fun clearSaveResult() {
        _saveResult.value = null
    }
    
    fun loadCurrentLocation() {
        // Implementation for GPS location if permission granted
    }
}

data class CreateInspectionFormState(
    val lotNumberError: String? = null,
    val quantityError: String? = null,
    val weightError: String? = null,
    val portLocationError: String? = null,
    val weatherError: String? = null
) {
    val isValid: Boolean get() = listOf(
        lotNumberError,
        quantityError,
        weightError,
        portLocationError,
        weatherError
    ).all { it == null }
}

data class CreateInspectionFormData(
    val lotNumber: String,
    val containerNumber: String?,
    val productTypeId: String,
    val quantity: Double,
    val weight: Double,
    val unit: String,
    val portLocation: String,
    val weatherConditions: String,
    val inspectorId: String,
    val notes: String?
)

sealed class CreateInspectionResult {
    data class Success(val inspectionId: String, val isDraft: Boolean) : CreateInspectionResult()
    data class Error(val message: String) : CreateInspectionResult()
}