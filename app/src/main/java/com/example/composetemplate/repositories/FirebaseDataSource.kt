package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.requests.FirebaseUserRequests
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * In the MVVM architecture, the DataSource layer is responsible for providing data to the repository
 * from various sources, such as remote APIs, local databases, or in-memory caches.
 * allowing the repository to seamlessly aggregate and supply data to the ViewModel without worrying about the underlying data origins or access mechanisms.
 */
class FirebaseDataSource() : AuthDBServiceable {

    /**
     *  Firebase DB Functionality
     */
    //auth instance
    var auth: FirebaseAuth = Firebase.auth
    private var accessToken: String = ""

    //initialize the access token for the firebase network call
    private fun getFirebaseUserAccessToken(successCallback: SuccessCallback) {
        if (accessToken.isEmpty()) {
            auth.currentUser?.let { currentUser ->
                currentUser.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result.token.isNullOrEmpty().not()) {
                        val accessToken = task.result.token ?: ""
                        this.accessToken = accessToken
                        successCallback(true, null)
                    } else {
                        successCallback(false, task.exception)
                    }
                }
            } ?: run {
                successCallback(false, Exception("No current user"))
                return
            }
        } else {
            successCallback(true, null)
        }
    }

    //first we get the access token of firebase to get the user.
    // if its ok we pass in the successCallback to pass the layer that have the network layer for server call.
    // the naming get user its if there is a different wat to implement the interface that we dont need th access token
    override fun getUser(successCallback: SuccessCallback) {
        getFirebaseUserAccessToken { success, e ->
            val id = auth.currentUser?.uid
            if (!success || id.isNullOrEmpty()) {
                successCallback(false, e)
                return@getFirebaseUserAccessToken
            }
            successCallback(true,null)
        }
    }

    //we fetch the access token of firebase and pass the user to the callback, to send the request to firebase
    override fun createOrUpdateUser(user: User, loginCallback: LoginCallback) {
        getFirebaseUserAccessToken { success, e ->
            val id = auth.currentUser?.uid
            if (!success || id.isNullOrEmpty()) {
                loginCallback(null, e)
                return@getFirebaseUserAccessToken
            }
            loginCallback(user, null)
        }
    }

    // the request for the network call to patch the user to realtime database
    fun createOrUpdateUserRequestForFirebase(user: User): FirebaseUserRequests.CreateOrUpdateUser {
        val queries = mutableMapOf<String, String>().apply {
            this["auth"] = this@FirebaseDataSource.accessToken
        }
        return FirebaseUserRequests.CreateOrUpdateUser(queries = queries, body = user)
    }

    // the request for the network call to get the user from realtime database
    fun getUserRequest(): FirebaseUserRequests.GetUser {
        val queries = mutableMapOf<String, String>().apply {
            this["auth"] = this@FirebaseDataSource.accessToken
        }
        return FirebaseUserRequests.GetUser(queries = queries)
    }

    fun logOut() {
        auth.signOut()
    }

}
