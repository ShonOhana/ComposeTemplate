package com.example.composetemplate.data.remote

import android.content.Context
import com.example.composetemplate.data.remote.base.BaseNetworking
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.managers.ConnectivityManager
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.exceptions.NoInternetConnectionException
import com.example.composetemplate.utils.exceptions.UnauthorizedException

/** This class manages all networking operations.
In our project, we anticipate the existence of multiple networking classes tailored for specific services such as FirebaseNetworking, AWSNetworking, and others.
Each networking class is expected to inherit from BaseNetworking and implement the ServerAuthenticator interface */

class Networking(private val application: Context) : BaseNetworking(), ServerAuthenticator {
    
    override val baseUrl: String = FirebaseConfigProvider.getData(FirebaseConfigProvider.RemoteConfigValues.BASE_URL)

    /** This function intercept the request if there is no internet connection,
     * or the user is unauthorized.
     * Note: the function is throw an informative exception if there is a problem.
     *  */
    override fun applyInterceptor(): Boolean {
        try {
            if (getAuthenticationStatus() != ServerAuthenticator.AuthenticationStatus.CONNECTED) {
                throw UnauthorizedException()
            }
            if (!ConnectivityManager(application).isDeviceOnline()) {
                throw NoInternetConnectionException()
            }
        } catch (exception: Exception) {
            LogsManager().logServerError(exception.message)
            return false
        }
        return true
    }

    /** Implement the authentication rules as you wish */
    override fun getAuthenticationStatus(): ServerAuthenticator.AuthenticationStatus {
        // TODO: implement
        return ServerAuthenticator.AuthenticationStatus.CONNECTED
    }
}
