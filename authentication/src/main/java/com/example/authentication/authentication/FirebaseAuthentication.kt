package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
import com.example.authentication.results.ResultOfSignUpProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): Authentication {

    override fun isSignedIn(): Boolean =
        firebaseAuth.currentUser != null && firebaseAuth.currentUser!!.isEmailVerified

    override suspend fun signUp(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess {

        return suspendCoroutine { continuation ->

            firebaseAuth
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

            val user = firebaseAuth.currentUser!!
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

    override suspend fun signIn(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess {

        return suspendCoroutine { continuation ->

            firebaseAuth
                .signInWithEmailAndPassword(
                    credentials.email,
                    credentials.password
                )
                .addOnCompleteListener { task ->

                    val currentUser = firebaseAuth.currentUser

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
}