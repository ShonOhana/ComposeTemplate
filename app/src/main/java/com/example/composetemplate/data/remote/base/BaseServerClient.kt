package com.example.composetemplate.data.remote.base

import io.ktor.client.HttpClient

/**
 * This class define what the application expect expects a client.
 * Each client should implement the following things,
 * For example each client should obsessively have a client parameter and sendRequest function.
 * Regardless of the specific client being used, the process remains the same.
 * initiating a server request, the sendRequest function is called.
 * If there are additional logic requirements that any client needs, please add them here.
 * */
interface BaseServerClient {

    val baseUrl:String

    val client: HttpClient
    suspend fun sendRequest(request: BaseRequest): Any?
}