package eu.project.aiesla.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthentication: Authentication {

    private val auth: FirebaseAuth = Firebase.auth

    override fun isSignedIn(): Boolean =
        auth.currentUser != null && auth.currentUser!!.isEmailVerified

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): ResultOfSignUpProcess {

        return suspendCoroutine { continuation ->

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        continuation.resume(value = ResultOfSignUpProcess.Ok)
                    }

                    else {

                        continuation.resume(value = ResultOfSignUpProcess.UnidentifiedException)
                    }
                }
        }
    }

    override suspend fun sendSignUpVerificationEmail(): ResultOfVerificationProcess {

        return suspendCoroutine { continuation ->

            val user = auth.currentUser!!
            user.sendEmailVerification()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        continuation.resume(value = ResultOfVerificationProcess.Ok)
                    }

                    else {

                        continuation.resume(value = ResultOfVerificationProcess.UnidentifiedException)
                    }
                }
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): ResultOfSignInProcess {

        return suspendCoroutine { continuation ->

            auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    val currentUser = auth.currentUser

                    if (task.isSuccessful && currentUser != null && currentUser.isEmailVerified) {

                        continuation.resume(value = ResultOfSignInProcess.Ok)
                    }

                    else {

                        continuation.resume(value = ResultOfSignInProcess.UnidentifiedException)
                    }
                }
        }
    }
}