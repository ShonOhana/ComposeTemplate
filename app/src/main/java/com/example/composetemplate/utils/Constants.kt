package com.example.composetemplate.utils

class Constants {

    companion object {

        // Time

        const val ONE_SECOND = 1000L
        const val ONE_MINUTE = ONE_SECOND * 60
        const val ONE_HOUR = ONE_MINUTE * 60

        // Network
        const val BASE_URL = "newsapi.org"
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

        // Database
        const val NO_IMPLEMENT_DB_OPERATION_EXCEPTION = "You haven't implement database operation. Please notice to implement in your Dao. "

        // DataStore
        // Files
        const val APP_DATA_STORE = "app_data_store"
        // Keys
        const val DS_TEST_KEY = "test_key"


    }
}