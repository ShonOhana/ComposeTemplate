package com.example.composetemplate.data.remote.requests

import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.data.remote.confing.remoteConfigVal
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import io.ktor.http.HttpMethod

sealed class FirebaseUserRequests {
    class CreateOrUpdateUser(
        override val method: HttpMethod = HttpMethod.Patch,
        override val path: String? = "users/${Firebase.auth.uid}.json",
        override val queries: MutableMap<String, String>? = null,
        override val headers: MutableMap<String, String>? = null,
        override val body: Any?,
        override val timeout: Long? = null,
        override val baseUrl: String = FirebaseConfigProvider.getData(remoteConfigVal.BASE_FIREBASE_URL),
        override val addToken: Boolean = true
    ) : BaseRequest

    class GetUser(
        override val method: HttpMethod = HttpMethod.Get,
        override val path: String? = "users/${Firebase.auth.uid}.json",
        override val queries: MutableMap<String, String>? = null,
        override val headers: MutableMap<String, String>? = null,
        override val body: Any? = null,
        override val timeout: Long? = null,
        override val baseUrl: String =  FirebaseConfigProvider.getData(remoteConfigVal.BASE_FIREBASE_URL),
        override val addToken: Boolean = true
    ) : BaseRequest

}