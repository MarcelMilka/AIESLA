package eu.project.aiesla.auth.authenticationManager

import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential

interface AuthenticationManager {

    fun isSignedIn(): Boolean

    fun signIn(credentials: EmailAndPasswordCredentials)

    fun signUp(credentials: EmailAndPasswordCredentials)

    fun sendPasswordRecoveryEmail(email: EmailCredential)

    fun signOut()
}