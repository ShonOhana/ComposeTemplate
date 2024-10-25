package com.example.composetemplate.data.remote.requests

import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.data.remote.confing.remoteConfigVal
import com.example.composetemplate.utils.Constants
import io.ktor.http.HttpMethod

/**
 * For each type of request, we will create a sealed class.
 * Inside the sealed class, we will add subclasses that implement the BaseRequest interface and populate the data for the request.
 * Here is an example: */
sealed class ExampleRequests {
    class ExampleRequest(
        override val method: HttpMethod = HttpMethod.Get,
        override val path: String? = Constants.GET_SOURCES_PATH,
        override val queries: MutableMap<String, String> = mutableMapOf<String, String>().apply {
            this["country"] = "us"
            this["apiKey"] = "d83745a6406d4ac680c6325e7fffeced"
        },
        override val headers: MutableMap<String, String>? = null,
        override val body: Any? = null,
        override val timeout: Long? = null,
        override val baseUrl: String = FirebaseConfigProvider.getData(remoteConfigVal.BASE_URL),
        override val addToken: Boolean = false
    ) : BaseRequest

}