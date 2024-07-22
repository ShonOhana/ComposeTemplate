package com.example.composetemplate.utils.enums

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Enum class representing different types of data that can be stored in the DataStore.
 */
enum class DataStoreType {
    STRING, INTEGER, FLOAT, BOOLEAN, LONG, DOUBLE;

    /**
     * Returns the appropriate Preferences.Key for the given key string based on the type.
     * @param key The key string used to retrieve the Preferences.Key.
     * @return The Preferences.Key corresponding to the data type.
     */
    fun getPreferencesKey(key: String): Preferences.Key<out Any> {
        return when (this) {
            STRING -> stringPreferencesKey(key)
            INTEGER -> intPreferencesKey(key)
            FLOAT -> floatPreferencesKey(key)
            BOOLEAN -> booleanPreferencesKey(key)
            LONG -> longPreferencesKey(key)
            DOUBLE -> doublePreferencesKey(key)
        }
    }
}