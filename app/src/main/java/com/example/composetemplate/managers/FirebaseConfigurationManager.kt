package com.example.composetemplate.managers

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

class FirebaseConfigurationManager : Configurable {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    override fun initConfiguration() {
        remoteConfig.setConfigSettingsAsync(configSettings)
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
}