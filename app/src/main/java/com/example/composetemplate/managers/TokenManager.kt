package com.example.composetemplate.managers

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
                throw Exception("Failed to fetch a valid token")
            }
        }
        return token ?: throw Exception("Token is unavailable")
    }

    private fun saveToken(newToken: String, newExpirationTime: Long?) {
        token = newToken
        expirationTime = newExpirationTime
    }

    private fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() >= (expirationTime ?: 0L)
    }
}
