package eu.project.aiesla.auth.authenticationManager

import eu.project.aiesla.auth.authentication.Authentication
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.di.IoDispatcher
import eu.project.aiesla.auth.results.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ProductionAuthenticationManager @Inject constructor(
    private val firebaseAuthentication: Authentication,
    @IoDispatcher private val coroutineScope: CoroutineScope
): AuthenticationManager {

    private var _signInProcess = MutableStateFlow<SignInProcess>(SignInProcess.Idle)
    override val signInProcess = _signInProcess.asStateFlow()

    private var _signUpProcess = MutableStateFlow<SignUpProcess>(SignUpProcess.Idle)
    override val signUpProcess = _signUpProcess.asStateFlow()

    private var _passwordRecoveryProcess = MutableStateFlow<PasswordRecoveryProcess>(PasswordRecoveryProcess.Idle)
    override val passwordRecoveryProcess = _passwordRecoveryProcess.asStateFlow()

    private val timeout = 10000L


    override fun isSignedIn(): Boolean = firebaseAuthentication.isSignedIn()

    override fun signIn(credentials: EmailAndPasswordCredentials) {

        coroutineScope
            .launch {
                try {

                    withTimeout(timeout) {

                        val signInProcess = async {

                            _signInProcess.emit(value = SignInProcess.Pending)
                            firebaseAuthentication.signInWithEmailAndPassword(credentials.email, credentials.password)
                        }

                        when (signInProcess.await()) {

                            ResultOfSignInProcess.Ok -> {

                                _signInProcess.emit(value = SignInProcess.Successful)
                            }

                            ResultOfSignInProcess.InvalidEmailFormat -> {

                                _signInProcess.emit(
                                    value = SignInProcess.Unsuccessful(
                                        cause = UnsuccessfulSignInProcessCause.InvalidEmailFormat
                                    )
                                )
                            }

                            ResultOfSignInProcess.PasswordIsIncorrect -> {

                                _signInProcess.emit(
                                    value = SignInProcess.Unsuccessful(
                                        cause = UnsuccessfulSignInProcessCause.PasswordIsIncorrect
                                    )
                                )
                            }

                            ResultOfSignInProcess.UnidentifiedException -> {

                                _signInProcess.emit(
                                    value = SignInProcess.Unsuccessful(
                                        cause = UnsuccessfulSignInProcessCause.UnidentifiedException
                                    )
                                )
                            }
                        }
                    }
                }

                catch (e: TimeoutCancellationException) {

                    _signInProcess.emit(
                        value = SignInProcess.Unsuccessful(
                            cause = UnsuccessfulSignInProcessCause.Timeout
                        )
                    )
                }

                catch (e: Exception) {

                    _signInProcess.emit(
                        value = SignInProcess.Unsuccessful(
                            cause = UnsuccessfulSignInProcessCause.UnidentifiedException
                        )
                    )
                }
            }
    }

    override fun signUp(credentials: EmailAndPasswordCredentials) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                val signUpProcess = async {

                    _signUpProcess.emit(value = SignUpProcess.Pending)
                    firebaseAuthentication.signUpWithEmailAndPassword(credentials.email, credentials.password)
                    ResultOfSignUpProcess.Ok
                }

                when (signUpProcess.await()) {

                    ResultOfSignUpProcess.Ok -> {

                        val verificationProcess = async {

                            firebaseAuthentication.sendSignUpVerificationEmail()
                        }

                        when (verificationProcess.await()) {

                            ResultOfVerificationProcess.Ok -> {

                                _signUpProcess.emit(value = SignUpProcess.Successful)
                            }

                            ResultOfVerificationProcess.UnidentifiedException -> {

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
                                cause = UnsuccessfulSignUpProcessCause.UnidentifiedException
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

    override fun sendPasswordRecoveryEmail(email: EmailCredential) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Pending)
                val resultOfPasswordRecoveryProcess =
                    firebaseAuthentication.sendPasswordRecoveryEmail(email = email)

                when (resultOfPasswordRecoveryProcess) {

                    ResultOfPasswordRecoveryProcess.Ok -> {

                        _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Successful)
                    }

                    ResultOfPasswordRecoveryProcess.InvalidEmailFormat -> {

                        _passwordRecoveryProcess.emit(
                            value = PasswordRecoveryProcess.Unsuccessful(
                                cause = UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat
                            )
                        )
                    }

                    ResultOfPasswordRecoveryProcess.UnidentifiedException -> {

                        _passwordRecoveryProcess.emit(
                            value = PasswordRecoveryProcess.Unsuccessful(
                                cause = UnsuccessfulPasswordRecoveryCause.UnidentifiedException
                            )
                        )
                    }
                }
            }
    }

    override fun resetStatesOfProcesses() {

        CoroutineScope(Dispatchers.IO).launch {

            _signInProcess.emit(value = SignInProcess.Idle)

            _signUpProcess.emit(value = SignUpProcess.Idle)

            _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Idle)
        }
    }

    override fun signOut() {

        CoroutineScope(Dispatchers.IO).launch {

            firebaseAuthentication.signOut()
        }
    }
}