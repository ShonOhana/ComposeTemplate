package com.example.composetemplate.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectivityManager(private val application: Context) {
    fun isWIFIConnected(): Boolean {
        return getNetworkCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    fun isCellularInternetConnected(): Boolean {
        return getNetworkCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            ?: false
    }

    fun isEthernetInternetConnected(): Boolean {
        return getNetworkCapabilities()?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            ?: false
    }

    private fun getNetworkCapabilities(): NetworkCapabilities? {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
    }

    fun isDeviceOnline(): Boolean {
        return isWIFIConnected() || isCellularInternetConnected() || isEthernetInternetConnected()
    }
}