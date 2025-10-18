package com.metalinspect.app.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    protected fun showError(message: String) {
        viewModelScope.launch {
            _error.emit(message)
        }
    }

    protected fun showMessage(message: String) {
        viewModelScope.launch {
            _message.emit(message)
        }
    }

    protected suspend fun <T> safeCall(
        call: suspend () -> T,
        onSuccess: (suspend (T) -> Unit)? = null,
        onError: (suspend (Exception) -> Unit)? = null
    ) {
        try {
            setLoading(true)
            val result = call()
            onSuccess?.invoke(result)
        } catch (e: Exception) {
            onError?.invoke(e) ?: showError(e.message ?: "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }
}
