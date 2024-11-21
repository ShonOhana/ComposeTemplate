package com.example.composetemplate.data.remote.analytics

import android.os.Bundle
import android.util.Log
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

    object AnalyticsConstants{
        const val ItemName = "item_name"
        const val Value = "value"
    }

    fun logAnalytics(analyticsParameters: AnalyticsParameters) {

        val bundle = Bundle()

        analyticsParameters.itemName?.let { bundle.putString(ItemName, it) }
        analyticsParameters.value?.let { bundle.putString(Value, it) }

        firebaseAnalytics.logEvent(analyticsParameters.event.rawValue, bundle)
    }
}