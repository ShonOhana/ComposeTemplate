package com.example.composetemplate.data.remote.errors

import com.example.composetemplate.data.models.local_models.ErrorType


/**
 * A sealed interface representing a contract for error types in the application.
 *
 * Classes or objects implementing this interface must define a specific [ErrorType],
 * allowing for consistent error handling and categorization across the application.
 *
 * By using a sealed interface, the implementation ensures all error types are
 * constrained within a known hierarchy, enabling better type safety and exhaustiveness checks.
 */
sealed interface Errorable {
    val errorType: ErrorType
}



