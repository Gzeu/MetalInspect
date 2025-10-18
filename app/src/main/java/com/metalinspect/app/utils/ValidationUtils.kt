package com.metalinspect.app.utils

import java.util.regex.Pattern

object ValidationUtils {
    
    private const val MIN_LOT_NUMBER_LENGTH = 3
    private const val MAX_LOT_NUMBER_LENGTH = 50
    private const val MIN_DESCRIPTION_LENGTH = 10
    private const val MAX_DESCRIPTION_LENGTH = 500
    private const val MAX_NOTES_LENGTH = 1000
    private const val MAX_CAPTION_LENGTH = 200
    private const val MIN_NAME_LENGTH = 2
    private const val MAX_NAME_LENGTH = 100
    
    private val LOT_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$")
    private val CONTAINER_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9_-]*$")
    
    fun validateLotNumber(lotNumber: String?): ValidationResult {
        return when {
            lotNumber.isNullOrBlank() -> ValidationResult.Error("Lot number is required")
            lotNumber.length < MIN_LOT_NUMBER_LENGTH -> ValidationResult.Error("Lot number must be at least $MIN_LOT_NUMBER_LENGTH characters")
            lotNumber.length > MAX_LOT_NUMBER_LENGTH -> ValidationResult.Error("Lot number cannot exceed $MAX_LOT_NUMBER_LENGTH characters")
            !LOT_NUMBER_PATTERN.matcher(lotNumber).matches() -> ValidationResult.Error("Lot number can only contain letters, numbers, hyphens and underscores")
            else -> ValidationResult.Success
        }
    }
    
    fun validateContainerNumber(containerNumber: String?): ValidationResult {
        return when {
            containerNumber.isNullOrBlank() -> ValidationResult.Success // Optional field
            containerNumber.length > MAX_LOT_NUMBER_LENGTH -> ValidationResult.Error("Container number cannot exceed $MAX_LOT_NUMBER_LENGTH characters")
            !CONTAINER_NUMBER_PATTERN.matcher(containerNumber).matches() -> ValidationResult.Error("Container number can only contain letters, numbers, hyphens and underscores")
            else -> ValidationResult.Success
        }
    }
    
    fun validateQuantity(quantity: String?): ValidationResult {
        return when {
            quantity.isNullOrBlank() -> ValidationResult.Error("Quantity is required")
            else -> {
                try {
                    val value = quantity.toDouble()
                    when {
                        value <= 0 -> ValidationResult.Error("Quantity must be greater than zero")
                        value > 1000000 -> ValidationResult.Error("Quantity cannot exceed 1,000,000")
                        else -> ValidationResult.Success
                    }
                } catch (e: NumberFormatException) {
                    ValidationResult.Error("Please enter a valid number")
                }
            }
        }
    }
    
    fun validateWeight(weight: String?): ValidationResult {
        return when {
            weight.isNullOrBlank() -> ValidationResult.Error("Weight is required")
            else -> {
                try {
                    val value = weight.toDouble()
                    when {
                        value <= 0 -> ValidationResult.Error("Weight must be greater than zero")
                        value > 100000 -> ValidationResult.Error("Weight cannot exceed 100,000 kg")
                        else -> ValidationResult.Success
                    }
                } catch (e: NumberFormatException) {
                    ValidationResult.Error("Please enter a valid number")
                }
            }
        }
    }
    
    fun validatePortLocation(portLocation: String?): ValidationResult {
        return when {
            portLocation.isNullOrBlank() -> ValidationResult.Error("Port location is required")
            portLocation.length < 3 -> ValidationResult.Error("Port location must be at least 3 characters")
            portLocation.length > MAX_NAME_LENGTH -> ValidationResult.Error("Port location cannot exceed $MAX_NAME_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateWeatherConditions(weatherConditions: String?): ValidationResult {
        return when {
            weatherConditions.isNullOrBlank() -> ValidationResult.Error("Weather conditions are required")
            weatherConditions.length < 3 -> ValidationResult.Error("Weather conditions must be at least 3 characters")
            weatherConditions.length > MAX_NAME_LENGTH -> ValidationResult.Error("Weather conditions cannot exceed $MAX_NAME_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateNotes(notes: String?): ValidationResult {
        return when {
            notes.isNullOrBlank() -> ValidationResult.Success // Optional field
            notes.length > MAX_NOTES_LENGTH -> ValidationResult.Error("Notes cannot exceed $MAX_NOTES_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateDefectDescription(description: String?): ValidationResult {
        return when {
            description.isNullOrBlank() -> ValidationResult.Error("Description is required")
            description.length < MIN_DESCRIPTION_LENGTH -> ValidationResult.Error("Description must be at least $MIN_DESCRIPTION_LENGTH characters")
            description.length > MAX_DESCRIPTION_LENGTH -> ValidationResult.Error("Description cannot exceed $MAX_DESCRIPTION_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validatePhotoCaption(caption: String?): ValidationResult {
        return when {
            caption.isNullOrBlank() -> ValidationResult.Success // Optional field
            caption.length > MAX_CAPTION_LENGTH -> ValidationResult.Error("Caption cannot exceed $MAX_CAPTION_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateInspectorName(name: String?): ValidationResult {
        return when {
            name.isNullOrBlank() -> ValidationResult.Error("Inspector name is required")
            name.length < MIN_NAME_LENGTH -> ValidationResult.Error("Name must be at least $MIN_NAME_LENGTH characters")
            name.length > MAX_NAME_LENGTH -> ValidationResult.Error("Name cannot exceed $MAX_NAME_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateCompanyName(company: String?): ValidationResult {
        return when {
            company.isNullOrBlank() -> ValidationResult.Error("Company name is required")
            company.length < MIN_NAME_LENGTH -> ValidationResult.Error("Company name must be at least $MIN_NAME_LENGTH characters")
            company.length > MAX_NAME_LENGTH -> ValidationResult.Error("Company name cannot exceed $MAX_NAME_LENGTH characters")
            else -> ValidationResult.Success
        }
    }
}