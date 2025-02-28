package eu.project.aiesla.auth.authenticationManager

import android.util.Log
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.SignInProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestAuthenticationManager: AuthenticationManager {

    private val _signInProcess = MutableStateFlow<SignInProcess>(SignInProcess.Idle)
    override val signInProcess: StateFlow<SignInProcess>
        get() = _signInProcess

    override fun isSignedIn(): Boolean = false

    override fun signIn(credentials: EmailAndPasswordCredentials) {

        Log.d("Halla!", "signIn: ")

        CoroutineScope(Dispatchers.Default)
            .launch {
                _signInProcess.emit(value = SignInProcess.Successful)
            }
    }

    override fun signUp(credentials: EmailAndPasswordCredentials) {}

    override fun sendPasswordRecoveryEmail(email: EmailCredential) {}

    override fun signOut() {}
}