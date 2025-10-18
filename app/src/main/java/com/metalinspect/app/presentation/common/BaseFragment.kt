package com.metalinspect.app.presentation.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    protected abstract fun setupViews()
    protected abstract fun observeViewModel()

    protected fun <T> Flow<T>.collectWithLifecycle(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        action: suspend (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                collect { action(it) }
            }
        }
    }

    protected fun showSnackbar(
        message: String,
        length: Int = Snackbar.LENGTH_SHORT,
        action: String? = null,
        actionListener: (() -> Unit)? = null
    ) {
        view?.let { rootView ->
            val snackbar = Snackbar.make(rootView, message, length)
            if (action != null && actionListener != null) {
                snackbar.setAction(action) { actionListener() }
            }
            snackbar.show()
        }
    }

    protected fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    protected fun showSuccess(message: String) {
        showSnackbar(message, Snackbar.LENGTH_SHORT)
    }
}
