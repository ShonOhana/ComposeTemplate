package com.example.composetemplate.managers

import com.example.composetemplate.R
import com.example.composetemplate.utils.Constants.Companion.CONFIG_FETCHED_FAILED
import com.example.composetemplate.utils.Constants.Companion.CONFIG_FETCHED_SUCCESSFULLY
import com.example.composetemplate.utils.Constants.Companion.CONFIG_UPDATED_FAILED
import com.example.composetemplate.utils.Constants.Companion.CONFIG_UPDATED_SUCCESSFULLY
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.interfaces.Configurable
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

/**
 * Manager class for handling Firebase Remote Config configurations.
 *
 * This class implements the [Configurable] interface, and is responsible for
 * initializing, fetching, updating, and loading default configuration values
 * from Firebase Remote Config. It ensures that configuration settings are
 * fetched periodically and updates are handled dynamically.
 * For using the data from the config we use FirebaseConfigProvider
 */

class FirebaseConfigurationManager : Configurable {

    /* Firebase Remote Config instance used for fetching and managing configuration data. */
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    /* Configuration settings for Firebase Remote Config with a minimum fetch interval. */
    private val configSettings = remoteConfigSettings {
        /* Minimum interval for fetching new config data */
        minimumFetchIntervalInSeconds = 3600
    }

    override fun initConfiguration() {
        remoteConfig.setConfigSettingsAsync(configSettings)
        loadDefaults()
        fetch()
        update()
    }

    override fun fetch() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                LogsManager().logMessage(
                    LogsManager.LogType.DEBUG,
                    LogsManager.LogTag.CONFIG,
                    "$CONFIG_FETCHED_SUCCESSFULLY $updated"
                )
            } else {
                LogsManager().logMessage(
                    LogsManager.LogType.ERROR,
                    LogsManager.LogTag.CONFIG,
                    CONFIG_FETCHED_FAILED
                )
            }
        }
    }

    override fun update() {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {

            override fun onUpdate(configUpdate: ConfigUpdate) {
                LogsManager().logMessage(
                    LogsManager.LogType.DEBUG,
                    LogsManager.LogTag.CONFIG,
                    "$CONFIG_UPDATED_SUCCESSFULLY ${configUpdate.updatedKeys}"
                )
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                LogsManager().logMessage(
                    LogsManager.LogType.ERROR,
                    LogsManager.LogTag.CONFIG,
                    "$CONFIG_UPDATED_FAILED ${error.code}"
                )
            }
        })
    }

    override fun loadDefaults() {
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
}