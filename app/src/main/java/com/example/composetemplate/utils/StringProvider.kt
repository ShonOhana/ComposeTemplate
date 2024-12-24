package com.example.composetemplate.utils

import androidx.compose.ui.unit.LayoutDirection
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider.getData
import com.example.composetemplate.data.remote.confing.remoteConfigVal
import com.google.gson.Gson
import java.util.Locale

/**
 * Enum class representing supported language options for the application.
 *
 * @property rawValue The raw language code used to identify the language (e.g., "en" for English, "iw" for Hebrew).
 */
enum class LanguageOption(val rawValue: String) {
    HEBREW("iw"), /* ISO language code for Hebrew */
    ENGLISH("en"); /* ISO language code for English */

    fun getLayoutDirection(): LayoutDirection {
        return when(this){
            HEBREW -> LayoutDirection.Rtl
            ENGLISH -> LayoutDirection.Ltr
        }
    }
}

/**
 * StringProvider is responsible for providing localized strings based on the current language setting.
 * It fetches and parses the strings from Firebase Remote Config for each supported language.
 */
object StringProvider {

    /* Maps to hold localized strings for English and Hebrew. */
    private var enStrings: Map<String, String> = hashMapOf()
    private var heStrings: Map<String, String> = hashMapOf()

    init {
        /* Initialize the localized strings for both languages. */
        heStrings = getLocalizedStrings(remoteConfigVal.STRING_HE) /* Hebrew strings from Firebase */
        enStrings = getLocalizedStrings(remoteConfigVal.STRING_EN) /* English strings from Firebase */
    }

    /**
     * Retrieves the localized string for the given key based on the current language setting.
     *
     * @param key The key for the desired localized string.
     * @return The localized string corresponding to the provided key. If not found, returns the key itself.
     */
    fun getString(key: String): String {
        return when (getCurrentLanguage()) {
            LanguageOption.HEBREW -> heStrings[key] ?: key /* Return Hebrew string or the key if not found. */
            LanguageOption.ENGLISH -> enStrings[key] ?: key /* Return English string or the key if not found. */
        }
    }

    /**
     * Fetches the localized strings from Firebase Remote Config based on the specified key.
     *
     * @param key The key corresponding to the Firebase Remote Config value containing the localized strings.
     * @return A map containing key-value pairs of localized strings.
     */
    private fun getLocalizedStrings(key: FirebaseConfigProvider.RemoteConfigValues): Map<String, String> {
        val jsonString: String = getData(key) /* Fetch the raw JSON string. */
        return parseJsonToMap(jsonString) /* Parse the JSON string into a map of key-value pairs. */
    }

    /**
     * Determines the current language option based on the device's locale.
     *
     * @return The [LanguageOption] representing the current language setting of the device.
     */
    fun getCurrentLanguage(): LanguageOption {
        val language = Locale.getDefault().language
        return language.convertToLanguageOption() /* Convert the raw language code to LanguageOption. */
    }

    /**
     * Extension function that converts a raw language code (String) to a [LanguageOption].
     *
     * @param languageCode The raw language code to convert (e.g., "en", "iw").
     * @return The corresponding [LanguageOption].
     */
    fun String.convertToLanguageOption(): LanguageOption {
        return when (this) {
            LanguageOption.HEBREW.rawValue -> LanguageOption.HEBREW /* If the language code is "iw", return HEBREW. */
            else -> LanguageOption.ENGLISH /* Default to English for any other language code. */
        }
    }

    /**
     * Parses a JSON string into a map of key-value pairs representing localized strings.
     *
     * @param jsonString The JSON string to parse.
     * @return A map containing the parsed key-value pairs.
     */
    private fun parseJsonToMap(jsonString: String): Map<String, String> {
        return Gson().fromJson(jsonString, Map::class.java) as Map<String, String>
    }
}