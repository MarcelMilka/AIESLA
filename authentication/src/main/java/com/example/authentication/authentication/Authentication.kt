package com.example.authentication.authentication

import com.example.authentication.credentials.CloudUid
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.ResultOfPasswordRecoveryProcess
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
import com.example.authentication.results.ResultOfSignUpProcess

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun getCloudUid(): CloudUid?

    suspend fun signUp(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess

    suspend fun sendSignUpVerificationEmail(): ResultOfSendingSignUpVerificationEmail

    suspend fun signIn(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess

    suspend fun sendPasswordRecoveryEmail(email: EmailCredential): ResultOfPasswordRecoveryProcess
}