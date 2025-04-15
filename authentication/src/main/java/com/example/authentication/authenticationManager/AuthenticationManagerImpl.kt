package com.example.authentication.authenticationManager

import com.example.authentication.authentication.Authentication
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.di.FirebaseAuthenticationQ
import com.example.authentication.di.RoomAuthenticationQ
import com.example.authentication.results.*
import com.example.datastore.data.OnboardingRepository
import com.example.datastore.model.UserOnboardingState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class AuthenticationManagerImpl @Inject constructor(
    val onboardingRepository: OnboardingRepository,
    @FirebaseAuthenticationQ val firebaseAuthentication: Authentication,
    @RoomAuthenticationQ val roomAuthentication: Authentication,
    val coroutineScope: CoroutineScope
): AuthenticationManager {

    private var _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.SignedOut)
    override val authenticationState = _authenticationState.asStateFlow()

    private var _signUpProcess = MutableStateFlow<SignUpProcess>(SignUpProcess.Idle)
    override val signUpProcess = _signUpProcess.asStateFlow()

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

    override fun signUp(credentials: EmailAndPasswordCredentials) {

        coroutineScope
            .launch {
                try {

                    withTimeout(timeout) {

                        val signUpProcess = async {

                            _signUpProcess.emit(value = SignUpProcess.Pending)

                            firebaseAuthentication.signUp(
                                credentials = EmailAndPasswordCredentials(
                                    email = credentials.email,
                                    password = credentials.password
                                )
                            )
                        }

                        when (signUpProcess.await()) {

                            ResultOfSignUpProcess.Ok -> {

                                val processOfSendingSignUpVerificationEmail = async {

                                    firebaseAuthentication.sendSignUpVerificationEmail()
                                }

                                when (processOfSendingSignUpVerificationEmail.await()) {

                                    ResultOfSendingSignUpVerificationEmail.Ok -> {

                                        _signUpProcess.emit(value = SignUpProcess.Successful)
                                    }

                                    ResultOfSendingSignUpVerificationEmail.UnidentifiedException -> {

                                        _signUpProcess.emit(
                                            value = SignUpProcess.Unsuccessful(
                                                cause = UnsuccessfulSignUpProcessCause.UnidentifiedException
                                            )
                                        )
                                    }
                                }
                            }

                            ResultOfSignUpProcess.InvalidEmailFormat -> {

                                _signUpProcess.emit(
                                    value = SignUpProcess.Unsuccessful(
                                        cause = UnsuccessfulSignUpProcessCause.InvalidEmailFormat
                                    )
                                )
                            }

                            ResultOfSignUpProcess.EmailIsAlreadyInUse -> {

                                _signUpProcess.emit(
                                    value = SignUpProcess.Unsuccessful(
                                        cause = UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse
                                    )
                                )
                            }

                            ResultOfSignUpProcess.UnidentifiedException -> {

                                _signUpProcess.emit(
                                    value = SignUpProcess.Unsuccessful(
                                        cause = UnsuccessfulSignUpProcessCause.UnidentifiedException
                                    )
                                )
                            }
                        }
                    }
                }

                catch (e: TimeoutCancellationException) {

                    _signUpProcess.emit(
                        value = SignUpProcess.Unsuccessful(
                            cause = UnsuccessfulSignUpProcessCause.Timeout
                        )
                    )
                }

                catch (e: Exception) {

                    _signUpProcess.emit(
                        value = SignUpProcess.Unsuccessful(
                            cause = UnsuccessfulSignUpProcessCause.UnidentifiedException
                        )
                    )
                }
            }
    }

    override fun resetProcesses() {

        coroutineScope.launch {

            _signUpProcess.emit(value = SignUpProcess.Idle)
        }
    }
}