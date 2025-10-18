package com.metalinspect.app.utils

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isValid: Boolean
        get() = this is Success
        
    val errorMessage: String?
        get() = when (this) {
            is Error -> message
            is Success -> null
        }
}