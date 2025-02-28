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
                    ResultOfSignInProcess.UnidentifiedException -> {

                        _signInProcess.emit(
                            value = SignInProcess.Unsuccessful(
                                cause = UnsuccessfulSignInProcessCause.EXEMPLARY_CAUSE
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
                                        cause = UnsuccessfulSignUpProcessCause.EXEMPLARY_CAUSE
                                    )
                                )
                            }
                        }
                    }

                    ResultOfSignUpProcess.UnidentifiedException -> {

                        _signUpProcess.emit(
                            value = SignUpProcess.Unsuccessful(
                                cause = UnsuccessfulSignUpProcessCause.EXEMPLARY_CAUSE
                            )
                        )
                    }
                }
            }
    }

    override fun sendPasswordRecoveryEmail(email: EmailCredential) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                val resultOfPasswordRecoveryProcess =
                    firebaseAuthentication.sendPasswordRecoveryEmail(email = email)

                when (resultOfPasswordRecoveryProcess) {
                    ResultOfPasswordRecoveryProcess.Ok -> {}
                    ResultOfPasswordRecoveryProcess.UnidentifiedException -> {}
                }
            }
    }

    override fun signOut() {

        CoroutineScope(Dispatchers.IO).launch {

            firebaseAuthentication.signOut()
        }
    }
}