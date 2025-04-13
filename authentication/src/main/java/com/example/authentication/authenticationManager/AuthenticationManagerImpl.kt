package com.example.authentication.authenticationManager

import com.example.authentication.results.AuthenticationState
import com.example.datastore.data.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class AuthenticationManagerImpl @Inject constructor(
    val onboardingRepository: OnboardingRepository
): AuthenticationManager {

    private var _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.SignedOut)
    override val authenticationState = _authenticationState.asStateFlow()

    override fun checkAuthenticationState() {}
}