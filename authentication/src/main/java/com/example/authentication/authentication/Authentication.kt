package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSignUpProcess

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun signUp(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess
}