package com.example.authentication.authenticationManager

import com.example.authentication.results.AuthenticationState
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

    val authenticationState: StateFlow<AuthenticationState>

    fun checkAuthenticationState()
}