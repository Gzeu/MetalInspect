package com.metalinspect.app.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidationUtilsTest {
    
    @Test
    fun validateLotNumber_withValidInput_returnsSuccess() {
        // Valid lot numbers
        val validLotNumbers = listOf(
            "LOT-2025-001",
            "STEEL_BATCH_123",
            "ABC-123-XYZ",
            "lot123"
        )
        
        validLotNumbers.forEach { lotNumber ->
            val result = ValidationUtils.validateLotNumber(lotNumber)
            assertThat(result.isValid).isTrue()
        }
    }
    
    @Test
    fun validateLotNumber_withInvalidInput_returnsError() {
        // Test cases: input -> expected error message content
        val invalidCases = mapOf(
            null to "required",
            "" to "required",
            "   " to "required",
            "AB" to "at least", // Too short
            "A".repeat(51) to "cannot exceed", // Too long
            "LOT@2025" to "can only contain", // Invalid characters
            "LOT 2025" to "can only contain" // Space not allowed
        )
        
        invalidCases.forEach { (input, expectedErrorContent) ->
            val result = ValidationUtils.validateLotNumber(input)
            assertThat(result.isValid).isFalse()
            assertThat(result.errorMessage?.lowercase()).contains(expectedErrorContent)
        }
    }
    
    @Test
    fun validateQuantity_withValidInput_returnsSuccess() {
        val validQuantities = listOf("1", "100.5", "1000", "999999")
        
        validQuantities.forEach { quantity ->
            val result = ValidationUtils.validateQuantity(quantity)
            assertThat(result.isValid).isTrue()
        }
    }
    
    @Test
    fun validateQuantity_withInvalidInput_returnsError() {
        val invalidCases = mapOf(
            null to "required",
            "" to "required",
            "0" to "greater than zero",
            "-5" to "greater than zero",
            "1000001" to "cannot exceed",
            "not_a_number" to "valid number",
            "12.34.56" to "valid number"
        )
        
        invalidCases.forEach { (input, expectedErrorContent) ->
            val result = ValidationUtils.validateQuantity(input)
            assertThat(result.isValid).isFalse()
            assertThat(result.errorMessage?.lowercase()).contains(expectedErrorContent)
        }
    }
    
    @Test
    fun validateWeight_withValidInput_returnsSuccess() {
        val validWeights = listOf("0.1", "100", "2500.75", "99999")
        
        validWeights.forEach { weight ->
            val result = ValidationUtils.validateWeight(weight)
            assertThat(result.isValid).isTrue()
        }
    }
    
    @Test
    fun validateDefectDescription_withValidInput_returnsSuccess() {
        val validDescriptions = listOf(
            "Surface scratches visible on the left side of the steel sheet",
            "Dimensional deviation: width 2mm over specification limit",
            "Minor rust spots detected in corner areas, approximately 5% coverage"
        )
        
        validDescriptions.forEach { description ->
            val result = ValidationUtils.validateDefectDescription(description)
            assertThat(result.isValid).isTrue()
        }
    }
    
    @Test
    fun validateDefectDescription_withInvalidInput_returnsError() {
        val invalidCases = mapOf(
            null to "required",
            "" to "required",
            "Short" to "at least", // Too short
            "A".repeat(501) to "cannot exceed" // Too long
        )
        
        invalidCases.forEach { (input, expectedErrorContent) ->
            val result = ValidationUtils.validateDefectDescription(input)
            assertThat(result.isValid).isFalse()
            assertThat(result.errorMessage?.lowercase()).contains(expectedErrorContent)
        }
    }
    
    @Test
    fun validateInspectorName_withValidInput_returnsSuccess() {
        val validNames = listOf("John Doe", "Maria Silva", "A B", "Inspector123")
        
        validNames.forEach { name ->
            val result = ValidationUtils.validateInspectorName(name)
            assertThat(result.isValid).isTrue()
        }
    }
    
    @Test
    fun validateCompanyName_withValidInput_returnsSuccess() {
        val validCompanies = listOf(
            "Steel Corp Ltd",
            "MetalInspect Solutions",
            "ABC Industries",
            "Quality Assurance Inc."
        )
        
        validCompanies.forEach { company ->
            val result = ValidationUtils.validateCompanyName(company)
            assertThat(result.isValid).isTrue()
        }
    }
}