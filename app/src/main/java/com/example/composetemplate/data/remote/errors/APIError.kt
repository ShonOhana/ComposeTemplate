package com.example.composetemplate.data.remote.errors

import com.example.composetemplate.data.models.local_models.ErrorType

/**
 * Represents an API error with a specific type of error.
 *
 * This class is used to encapsulate error details coming from API responses,
 * allowing for a standardized way to handle errors throughout the application.
 *
 * @property errorType The type of error, represented by an [ErrorType] enum value.
 */
data class APIError(
    override val errorType: ErrorType
) : Errorable
