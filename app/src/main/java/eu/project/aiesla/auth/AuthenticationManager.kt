package eu.project.aiesla.auth

import kotlinx.coroutines.*
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
){

    fun isSignedIn(): Boolean = firebaseAuthentication.isSignedIn()

    fun signUp(credentials: Credentials) {

        CoroutineScope(Dispatchers.IO)
            .launch {

                val signUpProcess = async {

                    firebaseAuthentication.signUpWithEmailAndPassword(credentials.email, credentials.password)
                }

                when (signUpProcess.await()) {

                    ResultOfSignUpProcess.Ok -> {

                        val verificationProcess = async {

                            firebaseAuthentication.sendSignUpVerificationEmail()
                        }

                        when (verificationProcess.await()) {

                            ResultOfVerificationProcess.Ok -> {
                                // navigate to "ConfirmYourRegistrationScreen"
                            }

                            ResultOfVerificationProcess.UnidentifiedException -> {
                                // navigate to "AnErrorOccurRedWhileSendingVerificationEmailScreen"
                            }
                        }
                    }

                    ResultOfSignUpProcess.UnidentifiedException -> {
                        // display "an error occurred, ${error}"
                    }
                }
            }
    }

    fun signIn(credentials: Credentials) {

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

    fun signOut() {

        CoroutineScope(Dispatchers.IO).launch {

            firebaseAuthentication.signOut()
        }
    }
}