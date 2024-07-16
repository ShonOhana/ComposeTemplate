package com.example.composetemplate.utils.exceptions

import com.example.composetemplate.utils.Constants

class NoImplementationDbOperationException : Exception() {
    override val message = Constants.NO_IMPLEMENT_DB_OPERATION_EXCEPTION
}