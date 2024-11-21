package com.example.composetemplate.data.remote.analytics

import com.example.composetemplate.data.remote.analytics.AnalyticsManager.logAnalytics

class AnalyticsParameters(
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

class AnalyticsLogger {

    companion object {

        fun logLectureTopic(itemTitle: String) {
            logAnalytics(
                AnalyticsParameters(
                    AnalyticsParameters.Events.SELECT_LECTURE,
                    itemName = itemTitle
                )
            )
        }
    }
}