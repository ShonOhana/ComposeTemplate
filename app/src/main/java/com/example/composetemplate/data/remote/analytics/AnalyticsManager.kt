package com.example.composetemplate.data.remote.analytics

import android.os.Bundle
import com.example.composetemplate.data.remote.analytics.AnalyticsManager.AnalyticsConstants.ItemName
import com.example.composetemplate.data.remote.analytics.AnalyticsManager.AnalyticsConstants.Value
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsManager {

    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        firebaseAnalytics.setAnalyticsCollectionEnabled(false)
    }

    object AnalyticsConstants {
        const val ItemName = "item_name"
        const val Value = "value"
    }

    fun <T> logEvent(data: T, event: AnalyticsParameters.Events) {
        when (event) {
            AnalyticsParameters.Events.SELECT_LECTURE -> {
                logAnalytics(
                    AnalyticsParameters(
                        event,
                        itemName = data as? String
                    )
                )
            }
        }
    }

    private fun logAnalytics(analyticsParameters: AnalyticsParameters) {

        val bundle = Bundle()

        analyticsParameters.itemName?.let { bundle.putString(ItemName, it) }
        analyticsParameters.value?.let { bundle.putString(Value, it) }

        firebaseAnalytics.logEvent(analyticsParameters.event.rawValue, bundle)
    }
}

data class AnalyticsParameters(
    val event: Events,
    val itemName: String? = null,
    val value: String? = null,
) {

    enum class Events {
        SELECT_LECTURE;

        val rawValue: String
            get() = when (this) {
                SELECT_LECTURE -> this.name.lowercase()
            }
    }
}