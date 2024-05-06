package com.example.composetemplate.utils.exceptions

import com.example.composetemplate.utils.Constants.Companion.BAD_REQUEST_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.INTERNAL_SERVER_ERROR_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.NOT_FOUND_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.NO_INTERNET_CONNECTION_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.SERVICE_UNAVAILABLE_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.TIME_OUT_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.UNAUTHORIZED_EXCEPTION
import com.example.composetemplate.utils.Constants.Companion.UN_KNOWN_SERVER_EXCEPTION

enum class NetworkExceptions(val code:Int?){
    UnauthorizedException(401),
    BadRequestException(400),
    NotFoundException(404),
    TimeOutException(408),
    InternalServerErrorException(500),
    ServiceUnavailableException(503),
}
class NoInternetConnectionException : Exception() {
    override val message = NO_INTERNET_CONNECTION_EXCEPTION
}

class UnauthorizedException : Exception() {
    override val message = UNAUTHORIZED_EXCEPTION
}

class BadRequestException : Exception() {
    override val message = BAD_REQUEST_EXCEPTION
}

class NotFoundException : Exception() {
    override val message = NOT_FOUND_EXCEPTION
}
class TimeOutException : Exception() {
    override val message = TIME_OUT_EXCEPTION
}
class UnknownServerException(code:Int) : Exception() {
    override val message = "$UN_KNOWN_SERVER_EXCEPTION $code"
}
class InternalServerErrorException : Exception() {
    override val message = INTERNAL_SERVER_ERROR_EXCEPTION
}
class ServiceUnavailableException : Exception() {
    override val message = SERVICE_UNAVAILABLE_EXCEPTION
}