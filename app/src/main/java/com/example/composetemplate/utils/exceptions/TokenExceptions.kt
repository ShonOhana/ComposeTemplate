package com.example.composetemplate.utils.exceptions

import com.example.composetemplate.utils.Constants

class FailedToFetchTokenException : Exception() {
    override val message = Constants.FAILED_TO_FETCH_TOKEN_EXCEPTION
}

class UnavailableTokenException : Exception() {
    override val message = Constants.UNAVAILABLE_TOKEN_EXCEPTION
}