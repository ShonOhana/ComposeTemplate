package com.example.composetemplate.data.remote



/** The role of this interface is to provide server authentication functionality.
 *  This interface is implemented in the networking layer
 *  @see Networking*/

typealias authenticationStatus = ServerAuthenticator.AuthenticationStatus
interface ServerAuthenticator {

    enum class AuthenticationStatus {
        CONNECTED, DISCONNECTED
    }

    fun getAuthenticationStatus(): AuthenticationStatus
}