package com.metalinspect.app.utils

import android.util.Patterns

object ValidationUtils {
    
    fun validateLotNumber(lotNumber: String?): ValidationResult {
        return when {
            lotNumber.isNullOrBlank() -> ValidationResult.Error("Lot number is required")
            lotNumber.length > Constants.MAX_LOT_NUMBER_LENGTH -> 
                ValidationResult.Error("Lot number cannot exceed ${Constants.MAX_LOT_NUMBER_LENGTH} characters")
            !lotNumber.matches(Regex("^[A-Za-z0-9-_]+$")) -> 
                ValidationResult.Error("Lot number can only contain letters, numbers, hyphens and underscores")
            else -> ValidationResult.Success
        }
    }
    
    fun validateContainerNumber(containerNumber: String?): ValidationResult {
        return when {
            containerNumber.isNullOrBlank() -> ValidationResult.Success // Optional field
            containerNumber.length > Constants.MAX_CONTAINER_NUMBER_LENGTH -> 
                ValidationResult.Error("Container number cannot exceed ${Constants.MAX_CONTAINER_NUMBER_LENGTH} characters")
            !containerNumber.matches(Regex("^[A-Za-z0-9-_]+$")) -> 
                ValidationResult.Error("Container number can only contain letters, numbers, hyphens and underscores")
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
                        value > 1_000_000 -> ValidationResult.Error("Quantity cannot exceed 1,000,000")
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
                        value > 10_000_000 -> ValidationResult.Error("Weight cannot exceed 10,000,000")
                        else -> ValidationResult.Success
                    }
                } catch (e: NumberFormatException) {
                    ValidationResult.Error("Please enter a valid number")
                }
            }
        }
    }
    
    fun validatePortLocation(location: String?): ValidationResult {
        return when {
            location.isNullOrBlank() -> ValidationResult.Error("Port location is required")
            location.length < 2 -> ValidationResult.Error("Port location must be at least 2 characters")
            location.length > 100 -> ValidationResult.Error("Port location cannot exceed 100 characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateWeatherConditions(weather: String?): ValidationResult {
        return when {
            weather.isNullOrBlank() -> ValidationResult.Error("Weather conditions are required")
            weather.length < 3 -> ValidationResult.Error("Weather description must be at least 3 characters")
            weather.length > 100 -> ValidationResult.Error("Weather description cannot exceed 100 characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateInspectorName(name: String?): ValidationResult {
        return when {
            name.isNullOrBlank() -> ValidationResult.Error("Inspector name is required")
            name.length < 2 -> ValidationResult.Error("Name must be at least 2 characters")
            name.length > 50 -> ValidationResult.Error("Name cannot exceed 50 characters")
            !name.matches(Regex("^[a-zA-Z\\s.-]+$")) -> 
                ValidationResult.Error("Name can only contain letters, spaces, periods and hyphens")
            else -> ValidationResult.Success
        }
    }
    
    fun validateCompanyName(company: String?): ValidationResult {
        return when {
            company.isNullOrBlank() -> ValidationResult.Error("Company name is required")
            company.length < 2 -> ValidationResult.Error("Company name must be at least 2 characters")
            company.length > 100 -> ValidationResult.Error("Company name cannot exceed 100 characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateEmail(email: String?): ValidationResult {
        return when {
            email.isNullOrBlank() -> ValidationResult.Success // Optional field
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 
                ValidationResult.Error("Please enter a valid email address")
            else -> ValidationResult.Success
        }
    }
    
    fun validateNotes(notes: String?): ValidationResult {
        return when {
            notes == null -> ValidationResult.Success // Optional field
            notes.length > Constants.MAX_NOTES_LENGTH -> 
                ValidationResult.Error("Notes cannot exceed ${Constants.MAX_NOTES_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateDefectDescription(description: String?): ValidationResult {
        return when {
            description.isNullOrBlank() -> ValidationResult.Error("Defect description is required")
            description.length < 5 -> ValidationResult.Error("Description must be at least 5 characters")
            description.length > Constants.MAX_DEFECT_DESCRIPTION_LENGTH -> 
                ValidationResult.Error("Description cannot exceed ${Constants.MAX_DEFECT_DESCRIPTION_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validatePhotoCaption(caption: String?): ValidationResult {
        return when {
            caption == null -> ValidationResult.Success // Optional field
            caption.length > Constants.MAX_PHOTO_CAPTION_LENGTH -> 
                ValidationResult.Error("Caption cannot exceed ${Constants.MAX_PHOTO_CAPTION_LENGTH} characters")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isValid: Boolean
        get() = this is Success
    
    val errorMessage: String?
        get() = if (this is Error) message else null
}
