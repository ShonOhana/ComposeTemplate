package com.example.composetemplate.utils.exceptions

import com.example.composetemplate.utils.Constants.Companion.NO_DATA_EXCEPTION

class NoDataException : Exception() {
    override val message = NO_DATA_EXCEPTION
}