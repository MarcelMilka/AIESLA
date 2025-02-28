package eu.project.aiesla.auth

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationManager @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
){

    private var _authenticationState = MutableStateFlow<AuthenticationState>(
        AuthenticationState.Idle
    )
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState

    init {

        when (this.isSignedIn()) {
            true -> _authenticationState.value =  AuthenticationState.SignedIn
            false -> _authenticationState.value =  AuthenticationState.SignedOut
        }
    }

    fun isSignedIn(): Boolean = firebaseAuthentication.isSignedIn()

    fun signIn(credentials: EmailAndPasswordCredentials) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                val signInProcess = async {

                    firebaseAuthentication.signInWithEmailAndPassword(credentials.email, credentials.password)
                }

                when (signInProcess.await()) {

                    ResultOfSignInProcess.Ok -> {

                        // navigate to SignedInRoute
                    }
                    ResultOfSignInProcess.UnidentifiedException -> {

                        // display information "something went wrong"
                    }
                }
            }
    }

    fun signUp(credentials: EmailAndPasswordCredentials) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                val signUpProcess = async {

                    _authenticationState.emit(value = AuthenticationState.SigningUp)
                    firebaseAuthentication.signUpWithEmailAndPassword(credentials.email, credentials.password)
                }

                when (signUpProcess.await()) {

                    ResultOfSignUpProcess.Ok -> {

                        val verificationProcess = async {

                            firebaseAuthentication.sendSignUpVerificationEmail()
                        }

                        when (verificationProcess.await()) {

                            ResultOfVerificationProcess.Ok -> {

                                _authenticationState.emit(value = AuthenticationState.WaitingForVerification)
                            }

                            ResultOfVerificationProcess.UnidentifiedException -> {

                                _authenticationState.emit(value = AuthenticationState.FailedToSendVerificationEmail)
                            }
                        }
                    }

                    ResultOfSignUpProcess.UnidentifiedException -> {

                        _authenticationState.emit(value = AuthenticationState.FailedToSignUp)
                    }
                }
            }
    }

    fun sendPasswordRecoveryEmail(email: EmailCredential) {

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

    fun signOut() {

        CoroutineScope(Dispatchers.IO).launch {

            firebaseAuthentication.signOut()
        }
    }
}