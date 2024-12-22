package com.example.composetemplate.utils.extensions

import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.utils.exceptions.BadRequestException
import com.example.composetemplate.utils.exceptions.InternalServerErrorException
import com.example.composetemplate.utils.exceptions.NoInternetConnectionException
import com.example.composetemplate.utils.exceptions.NotFoundException
import com.example.composetemplate.utils.exceptions.ServiceUnavailableException
import com.example.composetemplate.utils.exceptions.TimeOutException
import com.example.composetemplate.utils.exceptions.UnauthorizedException

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.util.network.UnresolvedAddressException

val Throwable?.errorType: ErrorType
    get() = when(this) {
        is FirebaseAuthInvalidUserException -> ErrorType.USER_NOT_FOUND
        is FirebaseAuthInvalidCredentialsException -> ErrorType.INVALID_PASSWORD
        is FirebaseAuthUserCollisionException -> ErrorType.EMAIL_ALREADY_IN_USE
        is NoInternetConnectionException,
        is FirebaseNetworkException -> ErrorType.NETWORK_ERROR
        is UnauthorizedException -> ErrorType.UNAUTHORIZED
        is BadRequestException -> ErrorType.BAD_REQUEST
        is NotFoundException -> ErrorType.K404_NOT_FOUND
        is TimeOutException -> ErrorType.TIME_OUT
        is InternalServerErrorException -> ErrorType.INTERNAL_SERVER
        is ServiceUnavailableException -> ErrorType.SERVICE_UNAVAILABLE
        is UnresolvedAddressException -> ErrorType.UNRESOLVED_ADDRESS
        is ClientRequestException -> {
            when(response.status.value) {
                ErrorType.UNAUTHORIZED.statusCode -> ErrorType.UNAUTHORIZED
                ErrorType.BAD_REQUEST.statusCode -> ErrorType.BAD_REQUEST
                ErrorType.K404_NOT_FOUND.statusCode -> ErrorType.K404_NOT_FOUND
                ErrorType.TIME_OUT.statusCode -> ErrorType.TIME_OUT
                ErrorType.INTERNAL_SERVER.statusCode -> ErrorType.INTERNAL_SERVER
                ErrorType.SERVICE_UNAVAILABLE.statusCode -> ErrorType.SERVICE_UNAVAILABLE
                else -> ErrorType.UNKNOWN_ERROR
            }
        }
        else -> ErrorType.UNKNOWN_ERROR
    }
