package eu.project.aiesla.auth.authenticationManager

import eu.project.aiesla.auth.authentication.Authentication
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductionAuthenticationManager @Inject constructor(
    private val firebaseAuthentication: Authentication
): AuthenticationManager {

    private var _signInProcess = MutableStateFlow<SignInProcess>(SignInProcess.Idle)
    override val signInProcess = _signInProcess.asStateFlow()

    private var _signUpProcess = MutableStateFlow<SignUpProcess>(SignUpProcess.Idle)
    override val signUpProcess = _signUpProcess.asStateFlow()

    private var _passwordRecoveryProcess = MutableStateFlow<PasswordRecoveryProcess>(PasswordRecoveryProcess.Idle)
    override val passwordRecoveryProcess = _passwordRecoveryProcess.asStateFlow()


    override fun isSignedIn(): Boolean = firebaseAuthentication.isSignedIn()

    override fun signIn(credentials: EmailAndPasswordCredentials) {

        CoroutineScope(Dispatchers.IO)
            .launch {

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
                    ResultOfPasswordRecoveryProcess.UnidentifiedException -> {

                        _passwordRecoveryProcess.emit(
                            value = PasswordRecoveryProcess.Unsuccessful(
                                cause = UnsuccessfulPasswordRecoveryCause.EXEMPLARY_CAUSE
                            )
                        )
                    }
                }
            }
    }

    override fun signOut() {

        CoroutineScope(Dispatchers.IO).launch {

            firebaseAuthentication.signOut()
        }
    }
}