package com.metalinspect.app.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    protected fun showError(message: String) {
        _error.value = message
        _isLoading.value = false
    }
    
    protected fun showMessage(message: String) {
        _message.value = message
    }
    
    protected fun clearError() {
        _error.value = null
    }
    
    protected fun clearMessage() {
        _message.value = null
    }
    
    protected fun <T> safeCall(
        call: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit = { showError(it.message ?: "Unknown error") },
        showLoading: Boolean = true
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            if (exception is Exception) {
                onError(exception)
            } else {
                showError("An unexpected error occurred")
            }
        }
        
        viewModelScope.launch(exceptionHandler) {
            try {
                if (showLoading) setLoading(true)
                val result = call()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            } finally {
                if (showLoading) setLoading(false)
            }
        }
    }
}