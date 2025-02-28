package eu.project.aiesla.auth.authentication

import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.ResultOfPasswordRecoveryProcess
import eu.project.aiesla.auth.results.ResultOfSignInProcess
import eu.project.aiesla.auth.results.ResultOfSignUpProcess
import eu.project.aiesla.auth.results.ResultOfVerificationProcess

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun signUpWithEmailAndPassword(email: String, password: String): ResultOfSignUpProcess

    suspend fun signInWithEmailAndPassword(email: String, password: String): ResultOfSignInProcess

    suspend fun sendSignUpVerificationEmail(): ResultOfVerificationProcess

    suspend fun sendPasswordRecoveryEmail(email: EmailCredential): ResultOfPasswordRecoveryProcess

    suspend fun signOut()
}