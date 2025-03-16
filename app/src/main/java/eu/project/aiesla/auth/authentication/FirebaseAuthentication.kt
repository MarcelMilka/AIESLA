package eu.project.aiesla.auth.authentication

import com.google.firebase.Firebase
import com.google.firebase.auth.*
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.ResultOfPasswordRecoveryProcess
import eu.project.aiesla.auth.results.ResultOfSignInProcess
import eu.project.aiesla.auth.results.ResultOfSignUpProcess
import eu.project.aiesla.auth.results.ResultOfSendingSignUpVerificationEmail
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthentication @Inject constructor(): Authentication {

    private val auth: FirebaseAuth = Firebase.auth

    override fun isSignedIn(): Boolean =
        auth.currentUser != null && auth.currentUser!!.isEmailVerified

    override suspend fun signUpWithEmailAndPassword(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess {

        return suspendCoroutine { continuation ->

            auth
                .createUserWithEmailAndPassword(
                    credentials.email,
                    credentials.password
                )
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        continuation.resume(value = ResultOfSignUpProcess.Ok)
                    }

                    else {

                        when (task.exception) {

                            is FirebaseAuthInvalidCredentialsException -> {

                                continuation.resume(
                                    value = ResultOfSignUpProcess.InvalidEmailFormat
                                )
                            }

                            is FirebaseAuthUserCollisionException -> {

                                continuation.resume(
                                    value = ResultOfSignUpProcess.EmailIsAlreadyInUse
                                )
                            }

                            else -> {

                                continuation.resume(
                                    value = ResultOfSignUpProcess.UnidentifiedException
                                )
                            }
                        }
                    }
                }
        }
    }

    override suspend fun sendSignUpVerificationEmail(): ResultOfSendingSignUpVerificationEmail {

        return suspendCoroutine { continuation ->

            val user = auth.currentUser!!
            user.sendEmailVerification()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        continuation.resume(value = ResultOfSendingSignUpVerificationEmail.Ok)
                    }

                    else {

                        continuation.resume(value = ResultOfSendingSignUpVerificationEmail.UnidentifiedException)
                    }
                }
        }
    }

    override suspend fun signInWithEmailAndPassword(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess {

        return suspendCoroutine { continuation ->

            auth
                .signInWithEmailAndPassword(
                    credentials.email,
                    credentials.password
                )
                .addOnCompleteListener { task ->

                    val currentUser = auth.currentUser

                    if (task.isSuccessful && currentUser != null && currentUser.isEmailVerified) {

                        continuation.resume(value = ResultOfSignInProcess.Ok)
                    }

                    else {

                        val exceptionMessage = task.exception?.message

                        if (exceptionMessage != null) {

                            when {

                                exceptionMessage.contains("The email address is badly formatted") -> {

                                    continuation.resume(value = ResultOfSignInProcess.InvalidEmailFormat)
                                }

                                exceptionMessage.contains("The supplied auth credential is incorrect, malformed or has expired") -> {

                                    continuation.resume(value = ResultOfSignInProcess.PasswordIsIncorrect)
                                }

                                else -> {

                                    continuation.resume(value = ResultOfSignInProcess.UnidentifiedException)
                                }
                            }
                        }

                        else {

                            continuation.resume(value = ResultOfSignInProcess.UnidentifiedException)
                        }
                    }
                }
        }
    }

    override suspend fun sendPasswordRecoveryEmail(email: EmailCredential): ResultOfPasswordRecoveryProcess {

        return suspendCoroutine { continuation ->

            auth.sendPasswordResetEmail(email.email)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        continuation.resume(value = ResultOfPasswordRecoveryProcess.Ok)
                    }

                    else {

                        when (task.exception) {

                            is FirebaseAuthInvalidCredentialsException -> {

                                continuation.resume(
                                    value = ResultOfPasswordRecoveryProcess.InvalidEmailFormat
                                )
                            }

                            else -> {

                                continuation.resume(
                                    value = ResultOfPasswordRecoveryProcess.UnidentifiedException
                                )
                            }
                        }
                    }
                }
        }
    }

    override suspend fun signOut() {

        auth.signOut()
    }
}