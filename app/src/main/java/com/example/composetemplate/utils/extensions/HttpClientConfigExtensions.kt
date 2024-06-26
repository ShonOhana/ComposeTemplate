package com.example.composetemplate.utils.extensions

import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.exceptions.BadRequestException
import com.example.composetemplate.utils.exceptions.InternalServerErrorException
import com.example.composetemplate.utils.exceptions.NetworkExceptions
import com.example.composetemplate.utils.exceptions.NotFoundException
import com.example.composetemplate.utils.exceptions.ServiceUnavailableException
import com.example.composetemplate.utils.exceptions.TimeOutException
import com.example.composetemplate.utils.exceptions.UnauthorizedException
import com.example.composetemplate.utils.exceptions.UnknownServerException
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<CIOEngineConfig>.handleJson() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }, ContentType.Application.Json)
    }
}

fun HttpClientConfig<CIOEngineConfig>.handleErrors() {
    HttpResponseValidator {
        validateResponse { response ->
            val statusCode = response.status.value
            if (statusCode != 200) {
                NetworkExceptions.entries.forEach { exception ->
                    if (exception.code == statusCode) {
                        throw when (exception) {
                            NetworkExceptions.UnauthorizedException -> BadRequestException()
                            NetworkExceptions.BadRequestException -> UnauthorizedException()
                            NetworkExceptions.NotFoundException -> NotFoundException()
                            NetworkExceptions.TimeOutException -> TimeOutException()
                            NetworkExceptions.InternalServerErrorException -> InternalServerErrorException()
                            NetworkExceptions.ServiceUnavailableException -> ServiceUnavailableException()
                        }
                    }
                }
                throw UnknownServerException(statusCode)
            }
        }
    }
}

fun HttpClientConfig<CIOEngineConfig>.installLogger() {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                LogsManager().logMessage(
                    LogsManager.LogType.VERBOSE,
                    LogsManager.LogTag.SERVER,
                    message
                )
            }
        }
        level = LogLevel.ALL
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}