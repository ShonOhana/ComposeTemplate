package com.example.composetemplate.data.remote.confing

import com.example.composetemplate.utils.extensions.toSnakeCase
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import java.util.Locale

/**
 * Singleton provider for accessing Firebase Remote Config values.
 *
 * This class provides an easy way to retrieve various types of configuration values from Firebase Remote Config.
 * It uses an enum [RemoteConfigValues] to define keys, and the [getData] function to fetch values in a type-safe manner.
 */
object FirebaseConfigProvider {

    /**
     * Enum representing the keys used for Firebase Remote Config.
     * This enum defines the various configuration keys that can be fetched. The keys are converted
     * to small letters snake_case when accessing the corresponding values in Firebase Remote Config.
     * Note: this enum must be updated when the configuration is changed.
     */
    enum class RemoteConfigValues {
        /* Networking */
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