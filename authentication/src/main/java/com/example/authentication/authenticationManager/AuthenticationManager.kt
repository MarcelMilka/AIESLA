package com.example.authentication.authenticationManager

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.AuthenticationState
import com.example.authentication.results.PasswordRecoveryProcess
import com.example.authentication.results.SignInProcess
import com.example.authentication.results.SignUpProcess
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

    val authenticationState: StateFlow<AuthenticationState>
    val signUpProcess: StateFlow<SignUpProcess>
    val signInProcess: StateFlow<SignInProcess>
    val passwordRecoveryProcess: StateFlow<PasswordRecoveryProcess>

    fun checkAuthenticationState()

    fun signUp(credentials: EmailAndPasswordCredentials)

    fun signIn(credentials: EmailAndPasswordCredentials)

    fun sendPasswordRecoveryEmail(email: EmailCredential)

    fun resetProcesses()
}