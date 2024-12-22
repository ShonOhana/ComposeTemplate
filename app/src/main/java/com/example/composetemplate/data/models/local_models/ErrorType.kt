package com.example.composetemplate.data.models.local_models

import com.example.composetemplate.managers.StringsKeyManager
import com.example.composetemplate.managers.StringsKeyManager.UNRESOLVED_ADDRESS_EXCEPTION
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.BAD_REQUEST_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.INTERNAL_SERVER_ERROR_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.NOT_FOUND_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.SERVICE_UNAVAILABLE_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.TIME_OUT_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.UNAUTHORIZED_EXCEPTION


/**
 * Represents the various types of errors that can occur in the application.
 *
 * Each error type can be mapped to a specific HTTP status code and a message key,
 * which are used for providing appropriate feedback to the user or logging purposes.
 */
enum class ErrorType {
    USER_NOT_FOUND,
    EMAIL_ALREADY_IN_USE,
    INVALID_PASSWORD,
    NETWORK_ERROR,
    UNKNOWN_ERROR,
    BAD_REQUEST,
    K404_NOT_FOUND,
    TIME_OUT,
    INTERNAL_SERVER,
    NO_DATA,
    SERVICE_UNAVAILABLE,
    UNRESOLVED_ADDRESS,
    UNAUTHORIZED;

    /**
     * Retrieves the corresponding HTTP status code for the error type.
     *
     * @return The HTTP status code, or `null` if the error type is not associated with a specific status code.
     */
    val statusCode: Int?
        get() = when(this) {
            UNAUTHORIZED -> 401
            BAD_REQUEST -> 400
            K404_NOT_FOUND -> 404
            TIME_OUT -> 408
            INTERNAL_SERVER -> 500
            SERVICE_UNAVAILABLE -> 503
            USER_NOT_FOUND ,
            EMAIL_ALREADY_IN_USE,
            INVALID_PASSWORD,
            NETWORK_ERROR,
            NO_DATA,
            UNRESOLVED_ADDRESS,
            UNKNOWN_ERROR -> null
        }

    /**
     * Retrieves the corresponding message key for the error type.
     *
     * The message key can be used to look up localized error messages or for consistent error handling.
     *
     * @return The message key associated with the error type.
     */
    val messageKey: String
        get() = when(this){
            UNAUTHORIZED -> UNAUTHORIZED_EXCEPTION
            BAD_REQUEST -> BAD_REQUEST_EXCEPTION
            K404_NOT_FOUND -> NOT_FOUND_EXCEPTION
            TIME_OUT -> TIME_OUT_EXCEPTION
            INTERNAL_SERVER -> INTERNAL_SERVER_ERROR_EXCEPTION
            SERVICE_UNAVAILABLE -> SERVICE_UNAVAILABLE_EXCEPTION
            UNRESOLVED_ADDRESS -> UNRESOLVED_ADDRESS_EXCEPTION
            USER_NOT_FOUND  -> StringsKeyManager.USER_NOT_FOUND
            EMAIL_ALREADY_IN_USE -> StringsKeyManager.EMAIL_ALREADY_IN_USE
            INVALID_PASSWORD -> StringsKeyManager.INVALID_PASSWORD
            NETWORK_ERROR -> StringsKeyManager.NETWORK_ERROR
            NO_DATA -> StringsKeyManager.EMPTY_STATE_NO_LECTURES_MESSAGE
            UNKNOWN_ERROR -> Constants.UNKNOWN_EXCEPTION
        }
}

