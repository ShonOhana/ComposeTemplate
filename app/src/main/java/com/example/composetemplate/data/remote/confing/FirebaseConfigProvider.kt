package com.example.composetemplate.data.remote.confing

import com.example.composetemplate.utils.extensions.toSnakeCase
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import java.util.Locale

object FirebaseConfigProvider {

    enum class RemoteConfigValues {
        /** Networking */
        BASE_URL,
        BASE_FIREBASE_URL
    }

    inline fun <reified T> getData(key: RemoteConfigValues): T {
        return when (val value = Firebase.remoteConfig.getValue(key.name.lowercase(Locale.ROOT).toSnakeCase())) {
            Boolean::class.java -> value.asBoolean() as T
            String::class.java -> value.asString() as T
            Double::class.java -> value.asDouble() as T
            Long::class.java -> value.asLong() as T
            else -> return value.asString() as T
        }
    }
}