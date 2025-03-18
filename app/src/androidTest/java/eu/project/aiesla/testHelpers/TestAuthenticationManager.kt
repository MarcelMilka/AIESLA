package eu.project.aiesla.testHelpers

import android.util.Log
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.auth.results.SignUpProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestAuthenticationManager: AuthenticationManager {

    private val _signInProcess = MutableStateFlow<SignInProcess>(SignInProcess.Idle)
    override val signInProcess: StateFlow<SignInProcess>
        get() = _signInProcess

    private val _signUpProcess = MutableStateFlow<SignUpProcess>(SignUpProcess.Idle)
    override val signUpProcess: StateFlow<SignUpProcess>
        get() = _signUpProcess

    private val _passwordRecoveryProcess = MutableStateFlow<PasswordRecoveryProcess>(PasswordRecoveryProcess.Idle)
    override val passwordRecoveryProcess: StateFlow<PasswordRecoveryProcess>
        get() = _passwordRecoveryProcess


    override fun isSignedIn(): Boolean = false

    override fun signIn(credentials: EmailAndPasswordCredentials) {

        Log.d("Halla!", "signIn: ")

        CoroutineScope(Dispatchers.Default)
            .launch {
                _signInProcess.emit(value = SignInProcess.Successful)
            }
    }

    override fun signUp(credentials: EmailAndPasswordCredentials) {

        Log.d("Halla!", "signUp: ")

        CoroutineScope(Dispatchers.Default)
            .launch {
                _signUpProcess.emit(value = SignUpProcess.Successful)
            }
    }

    override fun sendPasswordRecoveryEmail(email: EmailCredential) {

        Log.d("Halla!", "sendPasswordRecoveryEmail: ")

        CoroutineScope(Dispatchers.Default)
            .launch {
                _passwordRecoveryProcess.emit(value = PasswordRecoveryProcess.Successful)
            }
    }

    override fun signOut() {}

    override fun resetProcesses() {}
}