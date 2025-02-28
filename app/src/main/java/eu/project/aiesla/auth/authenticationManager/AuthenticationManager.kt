package eu.project.aiesla.auth.authenticationManager

import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.auth.results.SignUpProcess
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

    val signInProcess: StateFlow<SignInProcess>
    val signUpProcess: StateFlow<SignUpProcess>

    fun isSignedIn(): Boolean

    fun signIn(credentials: EmailAndPasswordCredentials)

    fun signUp(credentials: EmailAndPasswordCredentials)

    fun sendPasswordRecoveryEmail(email: EmailCredential)

    fun signOut()
}