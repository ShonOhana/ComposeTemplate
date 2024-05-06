package com.example.composetemplate.managers

import com.example.composetemplate.data.remote.BaseRequest
import com.example.composetemplate.data.remote.base.BaseNetworking


/**
 * This class is responsible for managing all network operations.
 * Here, we will execute network requests and perform related tasks.
 * */
class NetworkManager(
    private val baseNetworking: BaseNetworking
) {

    suspend fun sendRequest(request: BaseRequest): Any? {
        return baseNetworking.sendRequest(request)
    }

}