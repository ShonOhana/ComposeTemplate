package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.data.remote.requests.FirebaseUserRequests
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch

/**
 * In the MVVM architecture, the DataSource layer is responsible for providing data to the repository
 * from various sources, such as remote APIs, local databases, or in-memory caches.
 * allowing the repository to seamlessly aggregate and supply data to the ViewModel without worrying about the underlying data origins or access mechanisms.
 */
class FirebaseDataSource: AuthDataSource {

    //auth instance
    var auth: FirebaseAuth = Firebase.auth

    // TODO: create tokens provider
    private var accessToken: String = ""

    //initialize the access token for the firebase network call
    // TODO: move the token logic to the authenticate or something
    // todo saving the accessToken may cause permission denaied error . For example if the token is valid for 5 minutes
    // so when we will send request after 5 minutes so accessToken.isEmpty() will return false but the token is not valid
    // so the request will failed. we need to create mechanise that refresh the token if the token is not valid

    private fun getFirebaseUserAccessToken(successCallback: SuccessCallback) {
        //todo: move to token manager
//        if (accessToken.isEmpty()) {
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
//                return
            }
//        }
//        else {
//            successCallback(true, null)
//        }
    }

    /** the request for the network call to patch the user to realtime database */
    fun createOrUpdateUserRequest(user: User): BaseRequest {
        val queries = mutableMapOf<String, String>().apply {
            this["auth"] = this@FirebaseDataSource.accessToken
        }
        return FirebaseUserRequests.CreateOrUpdateUser(queries = queries, body = user)
    }

    /** the request for the network call to get the user from realtime database */
    fun getUserRequest(): FirebaseUserRequests.GetUser {
        val queries = mutableMapOf<String, String>().apply {
            this["auth"] = this@FirebaseDataSource.accessToken
        }
        return FirebaseUserRequests.GetUser(queries = queries)
    }

    override fun logout() = auth.signOut()

    /** reset password using user email
     * this method send link (maybe to spam) to your email to reset your password
     * if task is successful you go to signInFragment to login with your new password you set from your link
     */
    override fun resetPassword(email: String, successCallback: SuccessCallback) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successCallback(true,null)
                }else {
                    successCallback(false,task.exception)
                }
            }
    }

    /** Create user in Auth functionality in firebase */
    override fun createUserWithEmailAndPassword(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // TODO: get token provider instead using only firebase
                        getFirebaseUserAccessToken { b, exception ->
                            if (b && exception == null)
                                loginCallback(user, null)
                            else
                                loginCallback(null, exception)
                        }
                    } else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    /** Sign in user in Auth functionality in firebase */
    override fun signInWithEmailAndPassword(
        user: User,
        password: String,
        loginCallback: LoginCallback
    ) {
        if (user.email.isEmpty().not()) {
            auth.signInWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // TODO: get token provider instead using only firebase
                        getFirebaseUserAccessToken { b, exception ->
                            if (b && exception == null)
                                loginCallback(user, null)
                            else
                                loginCallback(null, exception)
                        }
                    } else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    override fun getUser(loginCallback: LoginCallback) {
        /** In Token valid continue get user server call */
        getFirebaseUserAccessToken { b, exception ->
            loginCallback(null,exception)
        }
    }
}
