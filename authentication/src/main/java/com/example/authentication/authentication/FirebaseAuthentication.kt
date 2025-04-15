package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
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
}