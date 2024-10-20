package com.example.composetemplate.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//todo: ser to firebase exception after i map it
class ErrorManager {
    private val _errorMessages = MutableStateFlow<String?>(null)
    val errorMessages: StateFlow<String?> get() = _errorMessages

    fun setErrorMessage(message: String?) {
        _errorMessages.value = message
    }

    fun clearErrorMessage() {
        _errorMessages.value = null
    }
}
