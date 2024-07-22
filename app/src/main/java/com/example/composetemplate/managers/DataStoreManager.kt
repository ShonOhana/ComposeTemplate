package com.example.composetemplate.managers

import com.example.composetemplate.MainApplication
import com.example.composetemplate.utils.enums.DataStoreType
import com.example.composetemplate.utils.extensions.readValue
import com.example.composetemplate.utils.extensions.writeValue
import kotlinx.coroutines.flow.Flow

class DataStoreManager(private val application: MainApplication) {

    fun readDSValue(key: String, type: DataStoreType): Flow<Any?> {
        return application.readValue(key, type)
    }

    suspend fun writeDSValue(key: String, type: DataStoreType, value: Any) {
        application.writeValue(key, type, value)
    }
}