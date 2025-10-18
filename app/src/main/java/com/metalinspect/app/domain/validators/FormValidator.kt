package com.metalinspect.app.domain.validators

import com.metalinspect.app.utils.ValidationResult
import com.metalinspect.app.utils.ValidationUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormValidator @Inject constructor() {
    
    fun validateCreateInspectionForm(
        lotNumber: String?,
        containerNumber: String?,
        quantity: String?,
        weight: String?,
        unit: String?,
        portLocation: String?,
        weatherConditions: String?,
        productTypeId: String?,
        inspectorId: String?,
        notes: String?
    ): Map<String, ValidationResult> {
        
        val results = mutableMapOf<String, ValidationResult>()
        
        results["lotNumber"] = ValidationUtils.validateLotNumber(lotNumber)
        results["containerNumber"] = ValidationUtils.validateContainerNumber(containerNumber)
        results["quantity"] = ValidationUtils.validateQuantity(quantity)
        results["weight"] = ValidationUtils.validateWeight(weight)
        results["portLocation"] = ValidationUtils.validatePortLocation(portLocation)
        results["weatherConditions"] = ValidationUtils.validateWeatherConditions(weatherConditions)
        results["notes"] = ValidationUtils.validateNotes(notes)
        
        // Additional validations
        if (productTypeId.isNullOrBlank()) {
            results["productType"] = ValidationResult.Error("Product type is required")
        } else {
            results["productType"] = ValidationResult.Success
        }
        
        if (inspectorId.isNullOrBlank()) {
            results["inspector"] = ValidationResult.Error("Inspector is required")
        } else {
            results["inspector"] = ValidationResult.Success
        }
        
        if (unit.isNullOrBlank()) {
            results["unit"] = ValidationResult.Error("Unit is required")
        } else {
            results["unit"] = ValidationResult.Success
        }
        
        return results
    }
    
    fun validateInspectorForm(
        name: String?,
        company: String?,
        role: String?
    ): Map<String, ValidationResult> {
        
        val results = mutableMapOf<String, ValidationResult>()
        
        results["name"] = ValidationUtils.validateInspectorName(name)
        results["company"] = ValidationUtils.validateCompanyName(company)
        
        if (role.isNullOrBlank()) {
            results["role"] = ValidationResult.Error("Role is required")
        } else if (role.length < 2) {
            results["role"] = ValidationResult.Error("Role must be at least 2 characters")
        } else if (role.length > 50) {
            results["role"] = ValidationResult.Error("Role cannot exceed 50 characters")
        } else {
            results["role"] = ValidationResult.Success
        }
        
        return results
    }
    
    fun validateDefectForm(
        defectType: String?,
        description: String?,
        count: String?
    ): Map<String, ValidationResult> {
        
        val results = mutableMapOf<String, ValidationResult>()
        
        if (defectType.isNullOrBlank()) {
            results["defectType"] = ValidationResult.Error("Defect type is required")
        } else {
            results["defectType"] = ValidationResult.Success
        }
        
        results["description"] = ValidationUtils.validateDefectDescription(description)
        
        if (count.isNullOrBlank()) {
            results["count"] = ValidationResult.Error("Count is required")
        } else {
            try {
                val countValue = count.toInt()
                if (countValue <= 0) {
                    results["count"] = ValidationResult.Error("Count must be greater than zero")
                } else if (countValue > 1000) {
                    results["count"] = ValidationResult.Error("Count cannot exceed 1000")
                } else {
                    results["count"] = ValidationResult.Success
                }
            } catch (e: NumberFormatException) {
                results["count"] = ValidationResult.Error("Count must be a valid number")
            }
        }
        
        return results
    }
    
    fun isFormValid(validationResults: Map<String, ValidationResult>): Boolean {
        return validationResults.values.all { it.isValid }
    }
    
    fun getFirstError(validationResults: Map<String, ValidationResult>): String? {
        return validationResults.values
            .filterIsInstance<ValidationResult.Error>()
            .firstOrNull()?.message
    }
}