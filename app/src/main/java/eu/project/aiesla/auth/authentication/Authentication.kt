package eu.project.aiesla.auth.authentication

import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.ResultOfPasswordRecoveryProcess
import eu.project.aiesla.auth.results.ResultOfSignInProcess
import eu.project.aiesla.auth.results.ResultOfSignUpProcess
import eu.project.aiesla.auth.results.ResultOfSendingSignUpVerificationEmail

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun signUpWithEmailAndPassword(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess

    suspend fun signInWithEmailAndPassword(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess

    suspend fun sendSignUpVerificationEmail(): ResultOfSendingSignUpVerificationEmail

    suspend fun sendPasswordRecoveryEmail(email: EmailCredential): ResultOfPasswordRecoveryProcess

    suspend fun signOut()
}