package com.plcoding.credentialmanagerguidecompose

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.composetemplate.data.models.local_models.User


sealed interface GoogleAuth {
//    suspend fun googleSignUp(activity: Activity,user: User,password: String, loginCallback: LoginCallback)
//    suspend fun googleSignIn(activity: Activity,successCallback: SuccessCallback)
}

class GoogleCredentialManager : GoogleAuth {

//    lateinit var credentialManager: CredentialManager
//
//    private fun init(activity: Activity){
//        if(!::credentialManager.isInitialized){
//            credentialManager = CredentialManager.create(activity)
//        }
//    }
//    override suspend fun googleSignUp(activity: Activity,user: User,password: String, loginCallback: LoginCallback) {
//        return try {
//            init(activity)
//            credentialManager.createCredential(
//                context = activity,
//                request = CreatePasswordRequest(
//                    id = user.fullName,
//                    password = password
//                )
//            )
//            loginCallback(user,null)
//        } catch (e: CreateCredentialCancellationException) {
//            e.printStackTrace()
//            loginCallback(null,e)
//        } catch(e: CreateCredentialException) {
//            e.printStackTrace()
//            loginCallback(null,e)
//        }
//    }
//
//    override suspend fun googleSignIn(activity: Activity, successCallback: SuccessCallback) {
//        return try {
//            init(activity)
//            val credentialResponse = credentialManager.getCredential(
//                context = activity,
//                request = GetCredentialRequest(
//                    credentialOptions = listOf(GetPasswordOption())
//                )
//            )
//
//            val credential = credentialResponse.credential as? PasswordCredential
//                ?: return successCallback(false,Exception("No password credential"))
//
//            // Make login API call here with credential.id and credential.password
//
//            successCallback(true,null)
//        } catch(e: GetCredentialCancellationException) {
//            e.printStackTrace()
//            successCallback(false,e)
//        } catch(e: NoCredentialException) {
//            e.printStackTrace()
//            successCallback(false,e)
//        } catch(e: GetCredentialException) {
//            e.printStackTrace()
//            successCallback(false,e)
//        }
//    }
}