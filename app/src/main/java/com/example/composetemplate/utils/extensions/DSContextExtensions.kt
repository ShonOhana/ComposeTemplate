package com.example.composetemplate.utils.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.composetemplate.utils.Constants.Companion.APP_DATA_STORE
import com.example.composetemplate.utils.enums.DataStoreType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_DATA_STORE)

fun Context.readValue(key: String, type: DataStoreType): Flow<Any?> {
    return dataStore.data.map { preferences ->
        preferences[type.getPreferencesKey(key)]
    }
}

suspend fun Context.writeValue(key: String, type: DataStoreType, value: Any) {
    dataStore.edit { mutablePreferences ->
        when (type) {
            DataStoreType.STRING -> (value as? String)?.let {
                mutablePreferences[stringPreferencesKey(key)] = it
            }

            DataStoreType.INTEGER -> (value as? Int)?.let {
                mutablePreferences[intPreferencesKey(key)] = it
            }

            DataStoreType.FLOAT -> (value as? Float)?.let {
                mutablePreferences[floatPreferencesKey(key)] = it
            }

            DataStoreType.BOOLEAN -> (value as? Boolean)?.let {
                mutablePreferences[booleanPreferencesKey(key)] = it
            }

            DataStoreType.LONG -> (value as? Long)?.let {
                mutablePreferences[longPreferencesKey(key)] = it
            }

            DataStoreType.DOUBLE -> (value as? Double)?.let {
                mutablePreferences[doublePreferencesKey(key)] = it
            }
        }
    }
}
