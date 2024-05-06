package com.example.composetemplate.data.remote

import io.ktor.http.HttpMethod

interface BaseRequest {
    val method: HttpMethod
    val path: String?
    val queries: MutableMap<String, String>?
    val headers: MutableMap<String, String>?
    val body: Any?
    val timeout:Long?
}