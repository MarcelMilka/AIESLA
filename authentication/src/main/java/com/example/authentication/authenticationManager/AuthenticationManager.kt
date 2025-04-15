package com.example.authentication.authenticationManager

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.AuthenticationState
import com.example.authentication.results.SignUpProcess
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

    val authenticationState: StateFlow<AuthenticationState>
    val signUpProcess: StateFlow<SignUpProcess>

    suspend fun checkAuthenticationState()

    fun signUp(credentials: EmailAndPasswordCredentials)
}