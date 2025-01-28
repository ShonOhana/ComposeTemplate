package com.example.composetemplate.managers

import com.example.composetemplate.utils.exceptions.FailedToFetchTokenException
import com.example.composetemplate.utils.exceptions.UnavailableTokenException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * TokenManager is responsible for managing the application's authentication token.
 * It handles the retrieval, storage, and refreshing of tokens while ensuring thread-safety
 * and managing concurrent requests for token acquisition. When a token is expired,
 * it refreshes the token using a provided TokenFetcher implementation and queues any
 * pending requests until the new token is available.
 *
 * The class utilizes a Mutex to prevent race conditions during token refresh operations,
 * ensuring that multiple requests for the token do not lead to redundant fetches.
 *
 * @param tokenFetcher An implementation of the TokenFetcher interface, used to fetch new tokens
 *                     when the existing token is expired or unavailable.
 *
 * @throws UnavailableTokenException if no valid token is available when requested.
 * @throws FailedToFetchTokenException if the token fetching process fails.
 */
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
    private val mutex = Mutex() /* For handling concurrent token requests */
    private val pendingRequests = mutableListOf<suspend () -> Any?>() // Queue for pending requests

    suspend fun getToken(): String {
        if (!isTokenExpired()) {
            return token ?: throw UnavailableTokenException()
        }
        /* If token is expired, queue the request */
        return mutex.withLock {
            if (isTokenExpired()) {
                /* Queue this request to be processed after the token is refreshed */
                val pendingRequest = suspend {
                    if (token == null) {
                        throw UnavailableTokenException()
                    }
                    token
                }
                /* Add this request to the pending queue */
                addPendingRequest(pendingRequest)
                refreshToken()
            }
            token ?: throw UnavailableTokenException() /* Ensure token is returned if available */
        }
    }

    private suspend fun refreshToken() {
        try {
            val (newToken, newExpirationTime) = tokenFetcher.fetchToken()
            if (newToken != null) {
                saveToken(newToken, newExpirationTime)
            } else {
                throw FailedToFetchTokenException()
            }
        } finally {
            releasePendingRequests()
        }
    }

    private fun saveToken(newToken: String, newExpirationTime: Long?) {
        token = newToken
        expirationTime = newExpirationTime
    }

    fun clearToken() {
        token = null
        expirationTime = 0L
    }

    private fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() >= (expirationTime ?: 0L)
    }

    private fun addPendingRequest(request: suspend () -> Any?) {
        pendingRequests.add(request)
    }

    private suspend fun releasePendingRequests() {
        for (request in pendingRequests) {
            request.invoke()
        }
        pendingRequests.clear()
    }
}
