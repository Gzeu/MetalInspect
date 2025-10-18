package com.metalinspect.app.domain.validators

import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.utils.ValidationResult
import com.metalinspect.app.utils.ValidationUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectionValidator @Inject constructor() {
    
    fun validateInspection(inspection: Inspection): List<ValidationResult> {
        val results = mutableListOf<ValidationResult>()
        
        results.add(ValidationUtils.validateLotNumber(inspection.lotNumber))
        results.add(ValidationUtils.validateContainerNumber(inspection.containerNumber))
        results.add(ValidationUtils.validateQuantity(inspection.quantity.toString()))
        results.add(ValidationUtils.validateWeight(inspection.weight.toString()))
        results.add(ValidationUtils.validatePortLocation(inspection.portLocation))
        results.add(ValidationUtils.validateWeatherConditions(inspection.weatherConditions))
        results.add(ValidationUtils.validateNotes(inspection.notes))
        
        // Custom business rules
        if (inspection.quantity <= 0) {
            results.add(ValidationResult.Error("Quantity must be greater than zero"))
        }
        
        if (inspection.weight <= 0) {
            results.add(ValidationResult.Error("Weight must be greater than zero"))
        }
        
        if (inspection.inspectorId.isBlank()) {
            results.add(ValidationResult.Error("Inspector must be selected"))
        }
        
        if (inspection.productTypeId.isBlank()) {
            results.add(ValidationResult.Error("Product type must be selected"))
        }
        
        return results
    }
    
    fun validateForCompletion(inspection: Inspection): ValidationResult {
        val validationResults = validateInspection(inspection)
        val errors = validationResults.filterIsInstance<ValidationResult.Error>()
        
        return if (errors.isNotEmpty()) {
            ValidationResult.Error("Cannot complete inspection: ${errors.first().message}")
        } else {
            ValidationResult.Success
        }
    }
    
    fun canStartInspection(inspection: Inspection): Boolean {
        return ValidationUtils.validateLotNumber(inspection.lotNumber).isValid &&
                ValidationUtils.validatePortLocation(inspection.portLocation).isValid &&
                inspection.inspectorId.isNotBlank() &&
                inspection.productTypeId.isNotBlank()
    }
}