package com.example.composetemplate.utils.extensions

import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.managers.TokenManager
import com.example.composetemplate.utils.Constants
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import org.koin.core.context.GlobalContext.get
import org.koin.java.KoinJavaComponent.inject

/** NOTE: to my understanding we use now in firebase server so all the queries should add the token.
 * so i Add it automatically to every call. if we dont need it we can edit with if condition or
 * move the get token fun to our @FirebaseNetwotkManager or create new class called @FirebaseAuthNetwotkManager*/
suspend fun HttpRequestBuilder.buildRequest(request: BaseRequest) {
    val tokenManager by inject<TokenManager>(TokenManager::class.java)
    val token = tokenManager.getToken()
    url {
        method = request.method
        protocol = URLProtocol.HTTPS
        host = request.baseUrl
        contentType(ContentType.Application.Json)
        request.path?.let { appendPathSegments(it) }
        val queries = request.queries ?: hashMapOf()
        queries.let {
            for (entry in it) {
                parameters.append(entry.key, entry.value)
            }
            parameters.append("auth",token) // add firebase token to each call
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