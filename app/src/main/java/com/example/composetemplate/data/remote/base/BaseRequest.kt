package com.example.composetemplate.data.remote.base

import io.ktor.http.HttpMethod

/** Every request has to return Base request and not its implementation! */
interface BaseRequest {
    val baseUrl: String
    val method: HttpMethod
    val path: String?
    val queries: MutableMap<String, String>?
    val headers: MutableMap<String, String>?
    val body: Any?
    val timeout:Long?
}