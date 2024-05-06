package com.example.composetemplate.utils

import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

/** This class serves as the logging module within our project.
 *  Rather than relying solely on ad-hoc Log.d statements scattered throughout the codebase, we centralize logging functionality here.
 *  This approach not only prepares us for future integration with server-side logging services
 *  but also ensures a more structured and organized logging process, enhancing readability and maintainability.
 * */
typealias tag = LogsManager.LogTag
typealias type = LogsManager.LogType
class LogsManager {

    enum class LogType {
        DEBUG, ERROR, VERBOSE
    }

    enum class LogTag {
        SERVER,GENERAL
    }

    fun logBundle(type: LogType, tag: LogTag, bundle: Bundle, function: String? = null) {
        log(type, tag, createJsonFomBundle(bundle, function))
    }

    fun logMessage(type: LogType, tag: LogTag, message: String) {
        log(type, tag, message)
    }

    fun logServerError(message: String?) {
        LogsManager().logMessage(
            LogType.ERROR,
            LogTag.SERVER,
            message ?: Constants.UNKNOWN_EXCEPTION
        )
    }

    private fun log(type: LogType, logTag: LogTag, message: String) {
        val tag = "Log_${logTag.name}"
        when (type) {
            LogType.DEBUG -> Log.d(tag, message)
            LogType.ERROR -> Log.e(tag, message)
            LogType.VERBOSE -> Log.v(tag, message)
        }
    }

    private fun createJsonFomBundle(bundle: Bundle, function: String?): String {
        val jsonObject = JsonObject()
        val gson = GsonBuilder().setPrettyPrinting().create()
        function?.let {
            jsonObject.addProperty("function", function)
        }
        for (key in bundle.keySet()) {
            jsonObject.addProperty(key, bundle.getString(key))
        }
        return gson.toJson(jsonObject)
    }
}