package com.example.composetemplate.data.remote.requests

import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import io.ktor.http.HttpMethod

sealed class FirebaseUserRequests {
    class createOrUpdateUser(
        override val method: HttpMethod = HttpMethod.Patch,
        override val path: String? = "users/${Firebase.auth.uid}.json",
        override val queries: MutableMap<String, String>,
        override val headers: MutableMap<String, String>? = null,
        override val body: Any?,
        override val timeout: Long? = null,
        override val baseUrl: String = Constants.FIREBASE_BASE_URL,
    ) : BaseRequest
}