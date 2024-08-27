package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.utils.LoginCallback


/**
 *
 * In the MVVM architecture, the Repository layer acts as a mediator between the ViewModel and data sources.
 * It aggregates data from multiple sources, such as remote APIs, databases, and caches, and provides a clean API for the ViewModel
 * This class contain the functionality of all login auth types
 */
class LoginRepository(
    private val firebaseDataSource: FirebaseDataSource,
) {

    /**
     *  Firebase email and password Auth Functionality
     */

    /** create user by email and password */
    fun createEmailPasswordUser(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            firebaseDataSource.auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseDataSource.createOrUpdateUserInDB(user, loginCallback)
                    } else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    /** sign in user by email and password */
    fun signInEmailPasswordUser(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            firebaseDataSource.auth.signInWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseDataSource.createOrUpdateUserInDB(user, loginCallback)
                    }  else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    /**
     * Create or update in database.
     * We can change the request to other server anytime we want id we change the service we use.
     * */
    fun createOrUpdateUserRequest(user: User): BaseRequest {
        return firebaseDataSource.createOrUpdateUserRequestForFirebase(user)
    }

}
