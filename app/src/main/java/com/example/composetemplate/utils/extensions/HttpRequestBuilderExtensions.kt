package com.example.composetemplate.utils.extensions

import com.example.composetemplate.data.remote.BaseRequest
import com.example.composetemplate.utils.Constants
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

fun HttpRequestBuilder.buildRequest(request: BaseRequest, baseUrl:String) {
    url {
        method = request.method
        protocol = URLProtocol.HTTPS
        host = baseUrl
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