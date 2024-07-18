package com.example.composetemplate.utils.enums

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

enum class DataStoreType {
    STRING, INTEGER, FLOAT, BOOLEAN, LONG, DOUBLE;

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