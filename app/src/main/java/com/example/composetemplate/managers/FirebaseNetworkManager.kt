package com.example.composetemplate.managers

import com.example.composetemplate.data.remote.base.BaseNetworking
import com.example.composetemplate.data.remote.base.BaseRequest

class FirebaseNetworkManager (
    private val baseNetworking: BaseNetworking,
) {

    suspend fun sendRequest(request: BaseRequest): Any? {
        return baseNetworking.sendRequest(request)
    }

}