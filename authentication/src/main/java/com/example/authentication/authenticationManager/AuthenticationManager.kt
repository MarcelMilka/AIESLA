package com.example.authentication.authenticationManager

import com.example.authentication.results.AuthenticationState
import com.example.authentication.results.SignUpProcess
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

    val authenticationState: StateFlow<AuthenticationState>

    suspend fun checkAuthenticationState()
}