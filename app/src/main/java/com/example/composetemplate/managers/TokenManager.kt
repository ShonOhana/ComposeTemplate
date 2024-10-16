package com.example.composetemplate.managers

import com.example.composetemplate.utils.exceptions.FailedToFetchTokenException
import com.example.composetemplate.utils.exceptions.UnavailableTokenException

interface TokenFetcher {
    suspend fun fetchToken(): TokenData
}

data class TokenData(
    val token: String?,
    val expirationTimeLong: Long?
)

class TokenManager(private val tokenFetcher: TokenFetcher) {
    private var token: String? = null
    private var expirationTime: Long? = 0

    suspend fun getToken(): String {
        if (isTokenExpired()) {
            val (newToken, newExpirationTime) = tokenFetcher.fetchToken()
            if (newToken != null) {
                saveToken(newToken, newExpirationTime)
            } else {
                throw FailedToFetchTokenException()
            }
        }
        return token ?: throw UnavailableTokenException()
    }

    private fun saveToken(newToken: String, newExpirationTime: Long?) {
        token = newToken
        expirationTime = newExpirationTime
    }

    private fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() >= (expirationTime ?: 0L)
    }
}
