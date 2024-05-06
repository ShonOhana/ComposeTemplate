package com.example.composetemplate.data.remote.base

import com.example.composetemplate.data.remote.BaseRequest
import com.example.composetemplate.data.remote.BaseServerClient
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.extensions.buildRequest
import com.example.composetemplate.utils.extensions.handleErrors
import com.example.composetemplate.utils.extensions.handleJson
import com.example.composetemplate.utils.extensions.installLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.request

/** This class serves as a provider for all networking requirements within the application.
For instance, it includes functionalities such as the network client and the capability to send requests.
Any networking-related class, such as `FirebaseNetworking`, should extend this class.
Additionally, this class offers a preconfigured base client for immediate use. If modifications are necessary, you can override either the client or the `sendRequest` function.
@see Networking implementation */
abstract class BaseNetworking : BaseServerClient {

    override val client: HttpClient = HttpClient(CIO) {
        /* By default, Ktor doesn't validate a response depending on its status code.
           When expectSuccess is set to true, Ktor will throw an exception if the response status code is not in the 2xx range (200 to 299).
           This allows us to handle error responses explicitly in your code.*/
        expectSuccess = true
        installLogger()
        handleErrors()
        handleJson()
        install(HttpTimeout)
    }

    override suspend fun sendRequest(request: BaseRequest): Any? {
        if (!applyInterceptor()) return null
        return try {
            client.request {
                buildRequest(request, baseUrl)
            }.body()
        } catch (exception: Exception) {
            LogsManager().logServerError(exception.message)
            null
        }
    }

    /** This function role is to intercept the request.
    This function is abstract and you should provide the body in your Networking subclass.
    If the function return false the request will not be sent. */
    abstract fun applyInterceptor(): Boolean
}