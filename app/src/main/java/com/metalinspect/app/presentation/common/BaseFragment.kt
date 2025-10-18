package com.metalinspect.app.presentation.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }
    
    protected open fun setupViews() {
        // Override in child fragments to setup UI components
    }
    
    protected open fun observeViewModel() {
        // Override in child fragments to observe ViewModel state
    }
    
    protected fun showError(message: String?) {
        if (!message.isNullOrBlank()) {
            view?.let {
                Snackbar.make(it, message, Snackbar.LENGTH_LONG)
                    .setAction("OK") { }
                    .show()
            }
        }
    }
    
    protected fun showSuccess(message: String?) {
        if (!message.isNullOrBlank()) {
            view?.let {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    
    protected fun <T> Flow<T>.collectWithLifecycle(action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            this@collectWithLifecycle.collect(action)
        }
    }
}