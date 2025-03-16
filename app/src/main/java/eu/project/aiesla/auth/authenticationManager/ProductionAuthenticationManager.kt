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
                            firebaseAuthentication.signInWithEmailAndPassword(
                                credentials = EmailAndPasswordCredentials(
                                    credentials.email,
                                    credentials.password
                                )
                            )
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

        coroutineScope
            .launch {
                try {

                    withTimeout(timeout) {

                        val signUpProcess = async {

                            _signUpProcess.emit(value = SignUpProcess.Pending)

                            firebaseAuthentication.signUpWithEmailAndPassword(
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

    override fun sendPasswordRecoveryEmail(email: EmailCredential) {

        coroutineScope
            .launch {
                try {

                    withTimeout(timeout) {

                        val resultOfPasswordRecoveryProcess = async {

                            _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Pending)
                            firebaseAuthentication.sendPasswordRecoveryEmail(email = email)
                        }

                        when (resultOfPasswordRecoveryProcess.await()) {

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

                catch (e: TimeoutCancellationException) {

                    _passwordRecoveryProcess.emit(
                        value = PasswordRecoveryProcess.Unsuccessful(
                            cause = UnsuccessfulPasswordRecoveryCause.Timeout
                        )
                    )
                }

                catch (e: Exception) {

                    _passwordRecoveryProcess.emit(
                        value = PasswordRecoveryProcess.Unsuccessful(
                            cause = UnsuccessfulPasswordRecoveryCause.UnidentifiedException
                        )
                    )
                }
            }
    }

    override fun resetStatesOfProcesses() {

        coroutineScope.launch {

            _signInProcess.emit(value = SignInProcess.Idle)

            _signUpProcess.emit(value = SignUpProcess.Idle)

            _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Idle)
        }
    }

    override fun signOut() {

        coroutineScope.launch {

            firebaseAuthentication.signOut()
        }
    }
}