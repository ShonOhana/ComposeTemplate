package com.example.composetemplate.data.remote.errors

import com.example.composetemplate.data.models.local_models.ErrorType

/**
 * Represents an authentication-related error with a specific type of error.
 *
 * This class is used to encapsulate errors encountered during authentication processes,
 * such as login, signup, or password recovery, providing a standardized way to handle
 * authentication errors in the application.
 *
 * @property errorType The type of error, represented by an [ErrorType] enum value.
 */
data class AuthError(
    override val errorType: ErrorType
) : Errorable