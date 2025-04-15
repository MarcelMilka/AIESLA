package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
import com.example.authentication.results.ResultOfSignUpProcess

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun signUp(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess

    suspend fun sendSignUpVerificationEmail(): ResultOfSendingSignUpVerificationEmail

    suspend fun signIn(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess
}