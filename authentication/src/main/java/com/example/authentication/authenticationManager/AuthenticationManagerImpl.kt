package com.example.authentication.authenticationManager

import com.example.authentication.authentication.Authentication
import com.example.authentication.di.FirebaseAuthenticationQ
import com.example.authentication.di.RoomAuthenticationQ
import com.example.authentication.results.AuthenticationState
import com.example.authentication.results.UnsuccessfulInitializationCause
import com.example.datastore.data.OnboardingRepository
import com.example.datastore.model.UserOnboardingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    val onboardingRepository: OnboardingRepository,
    @FirebaseAuthenticationQ val firebaseAuthentication: Authentication,
    @RoomAuthenticationQ val roomAuthentication: Authentication,
    val coroutineScope: CoroutineScope
): AuthenticationManager {

    private var _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.SignedOut)
    override val authenticationState = _authenticationState.asStateFlow()

    private val timeout = 10000L

    init {

        coroutineScope.launch {

            checkAuthenticationState()
        }
    }

    override suspend fun checkAuthenticationState() {

        coroutineScope
            .launch {
                try {

                    withTimeout(timeout) {

                        val onboardingState = onboardingRepository.checkOnboardingState() ?: UserOnboardingState(firstLaunchEver = null)

                        val authenticationState =
                            when (onboardingState.firstLaunchEver) {

                                true -> AuthenticationState.SignedIn

                                false -> {

                                    when(firebaseAuthentication.isSignedIn()) {

                                        true -> AuthenticationState.SignedIn

                                        false -> {

                                            when(roomAuthentication.isSignedIn()) {

                                                true -> AuthenticationState.SignedIn
                                                false -> AuthenticationState.SignedOut
                                            }
                                        }
                                    }
                                }

                                null -> AuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null)
                            }

                        _authenticationState.emit(value = authenticationState)
                    }
                }

                catch (e: TimeoutCancellationException) {

                    _authenticationState.emit(
                        value = AuthenticationState.Unsuccessful(
                            cause = UnsuccessfulInitializationCause.Timeout
                        )
                    )
                }

                catch (e: Exception) {

                    _authenticationState.emit(
                        value = AuthenticationState.Unsuccessful(
                            cause = UnsuccessfulInitializationCause.UnidentifiedException
                        )
                    )
                }
            }
    }
}