package com.example.composetemplate.utils

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.repositories.AuthInteractor

typealias LoginProvider = AuthInteractor.LoginProvider
class Constants {

    companion object {

        // Time

        const val ONE_SECOND = 1000L
        const val ONE_MINUTE = ONE_SECOND * 60
        const val ONE_HOUR = ONE_MINUTE * 60

        // Network
        const val GET_SOURCES_PATH = "v2/sources"
        const val TIME_OUT = 30 * ONE_SECOND

        // Exceptions
        const val UNKNOWN_EXCEPTION = "Unknown exception"
        const val NO_INTERNET_CONNECTION_EXCEPTION = "No internet connection available. Please check your network settings and try again when you have a stable internet connection"
        const val UNAUTHORIZED_EXCEPTION = "You are not authorized to access this resource. Please ensure you have the necessary permissions or sign in with appropriate credentials."
        const val BAD_REQUEST_EXCEPTION = "Bad request, please mke sure your request was sent properly"
        const val NOT_FOUND_EXCEPTION = "The requested resource could not be found. Please verify the URL or check if the resource has been removed or relocated"
        const val UN_KNOWN_SERVER_EXCEPTION = "An unknown error occurred on the server. Please try again later or contact the system administrator for assistance."
        const val INTERNAL_SERVER_ERROR_EXCEPTION = "An internal server error occurred while processing your request."
        const val SERVICE_UNAVAILABLE_EXCEPTION = "The service is temporarily unavailable due to high load or maintenance."
        const val TIME_OUT_EXCEPTION = "The server did not respond within the expected time. Please check your internet connection and try again."
        const val NO_DATA_EXCEPTION = "There are no data available."

        // Database
        const val NO_IMPLEMENT_DB_OPERATION_EXCEPTION = "You haven't implement database operation. Please notice to implement in your Dao. "

        //Token
        const val FAILED_TO_FETCH_TOKEN_EXCEPTION = "Failed to fetch a valid token"
        const val UNAVAILABLE_TOKEN_EXCEPTION = "Token is unavailable"

        // DataStore
        // Files
        const val APP_DATA_STORE = "app_data_store"
        // Keys
        const val DS_TEST_KEY = "test_key"


        //Strings
        const val LOGIN_TEXT = "Login"
        const val REGISTER_TEXT = "Don't have an account? Register!"
        const val AUTHENTICATION_ERROR_TEXT = "Authentication error - Please see all errors related to firebase auth"
        const val EMAIL_TEXT = "Email"
        const val PASSWORD_TEXT = "Password"
        const val FULL_NAME_TEXT = "Full Name"
        const val CONFIRM_PASSWORD_TEXT = "Confirm Password"
        const val HAVE_ACCOUNT_TEXT = "Already have an account? Login!"
        const val AUTH_WITH_GOOGLE = "with Google"
        const val FORGOT_PASSWORD_TEXT = "Enter your email"
        const val FORGOT_PASSWORD_TITLE = "Forgot Password"
        const val FORGOT_PASSWORD_BUTTON_TEXT = "Send email"
        const val LECTURE_CARD_PAST_TIME_TEXT = "This lecture as past"
        const val LECTURE_CARD_NEXT_LECTURE_TEXT = "Next Lecture"

        //EMPTY STATE
        const val EMPTY_STATE_ACCESS_DENIED_TITLE = "Access Denied"
        const val EMPTY_STATE_ACCESS_DENIED_SYNOPSIS = "looks like there are no valid token"
        const val EMPTY_STATE_NO_LECTURE_TITLE = "No lectures"
        const val EMPTY_STATE_NO_LECTURE_SYNOPSIS = "looks like there are no lectures scheduled"
        const val EMPTY_STATE_GENERAL_BUG_TITLE = "General bug"
        const val EMPTY_STATE_GENERAL_BUG_SYNOPSIS = "looks like there are something wrong with the server"
        const val EMPTY_STATE_NO_INTERNET_TITLE = "No Internet"
        const val EMPTY_STATE_NO_INTERNET_SYNOPSIS = "looks like there is no internet connected"

        //Logs Messages
        const val CONFIG_FETCHED_SUCCESSFULLY = "config params fetched successfully, is updated:"
        const val CONFIG_FETCHED_FAILED = "Config fetch failed"
        const val CONFIG_UPDATED_SUCCESSFULLY = "Updated keys:"
        const val CONFIG_UPDATED_FAILED = "Config update error with code:"
    }
}