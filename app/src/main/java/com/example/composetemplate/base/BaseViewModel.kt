package com.example.composetemplate.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.local.CacheData
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * BaseViewModel
 *
 * This is a base ViewModel class designed to manage common UI state functionality.
 * It provides state management for:
 * - UI state using `MutableStateFlow<UIState<T>>`.
 * - Progress indicator visibility based on the UI state.
 * - Dialog visibility control.
 *
 * Extend this class to reuse its functionality in other ViewModels.
 */
open class BaseViewModel : ViewModel() {

    /**
     * Represents the general UI state of the page.
     * This `StateFlow` emits updates to the UI state, which can be observed and handled.
     * - `UIState.Error`: Represents an error state.
     * - `UIState.Loading`: Represents a loading state.
     * - `UIState.Success`: Represents a successful state.
     */
    val uiState = MutableStateFlow<UIState<Any>?>(null)

    /**
     * Tracks the visibility of the progress indicator.
     * - `true`: Progress indicator is visible.
     * - `false`: Progress indicator is hidden.
     *
     * NOTICE: use FullScreenProgressBar(authViewModel.isProgressVisible) in the composable screen
     * if you want to see the dialog
     */
    private val _isProgressVisible = mutableStateOf(false)
    val isProgressVisible: Boolean by _isProgressVisible

    /**
     * Tracks the visibility of a dialog.
     * - `true`: Dialog is visible.
     * - `false`: Dialog is hidden.
     */
    private val _isDialogVisible = mutableStateOf(false)
    val isDialogVisible: Boolean by _isDialogVisible

    init {
        initPageState()
    }

    /**
     * Updates the visibility of a dialog.
     * @param isVisible Whether the dialog should be visible.
     */
    fun setDialogVisibility(isVisible: Boolean) {
        _isDialogVisible.value = isVisible
    }

    /**
     * Initializes the page state and sets the UI behavior based on the `uiState` flow.
     * - Hides the progress indicator on `Error` and `Success` states.
     * - Displays the progress indicator during the `Loading` state.
     */
    private fun initPageState() {
        viewModelScope.launch {
            uiState.collect { state ->
                if (state != null) {
                    when (state) {
                        is UIState.Error -> {
                            /* Hide progress indicator on error */
                            _isProgressVisible.value = false
                        }

                        is UIState.Loading -> {
                            /* Show progress indicator during loading */
                            _isProgressVisible.value = true
                        }

                        is UIState.Success -> {
                            /* If the data is user set it in the cache value */
                            (state.data as? User)?.let { user ->
                                CacheData.user = user
                            }
                            /* Hide progress indicator on success */
                            _isProgressVisible.value = false
                        }
                    }
                }
            }
        }
    }
}