package com.example.composetemplate.utils.extensions

import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.utils.Constants
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

fun HttpRequestBuilder.buildRequest(request: BaseRequest) {
    url {
        method = request.method
        protocol = URLProtocol.HTTPS
        host = request.baseUrl
        contentType(ContentType.Application.Json)
        request.path?.let { appendPathSegments(it) }
        request.queries?.let {
            for (entry in it) {
                parameters.append(entry.key, entry.value)
            }
        }
    }
    timeout { requestTimeoutMillis = request.timeout?: Constants.TIME_OUT }
    headers {
        request.headers?.let {
            for (entry in it) {
                append(entry.key, entry.value)
            }
        }
    }
    request.body?.let { setBody(it) }
}