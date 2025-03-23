package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.auth.results.UnsuccessfulPasswordRecoveryCause
import eu.project.aiesla.sharedUi.sharedElements.button.ButtonProceedViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.SignInEmailHintOptions
import eu.project.aiesla.sharedUi.sharedElements.textField.SignInEmailHintViewState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverYourPasswordScreenViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    val signInProcess = this.authenticationManager.passwordRecoveryProcess

    private val _email = MutableStateFlow(EmailCredential(""))
    val email = _email.asStateFlow()

    private val _stateOfEmailHint = MutableStateFlow<SignInEmailHintViewState>(SignInEmailHintViewState.Invisible)
    val stateOfEmailHint = _stateOfEmailHint.asStateFlow()

    private val _stateOfButtonProceed = MutableStateFlow<ButtonProceedViewState>(ButtonProceedViewState.Disabled)
    val stateOfButtonProceed = _stateOfButtonProceed.asStateFlow()



    init {

        viewModelScope.launch { authenticationManager.resetProcesses() }

        viewModelScope.launch { updateStateOfTheButtonProceed() }

        viewModelScope.launch { updateStateOfHint() }
    }

    fun updateEmail(email: EmailCredential) {

        _email.update { it.copy(email = email.email) }
        authenticationManager.resetProcesses()
        hideEmailHint()
    }

    fun getEmail(): EmailCredential = _email.value

    fun sendPasswordRecoveryEmail() {

        val email = this.getEmail()
        authenticationManager.sendPasswordRecoveryEmail(email)
    }


    // 'state operators'
    private suspend fun updateStateOfTheButtonProceed() {

        combine(
            _email,
            authenticationManager.passwordRecoveryProcess,
            _stateOfEmailHint
        ) { email, passwordRecoveryProcess, emailHint ->

            val state =
                when(passwordRecoveryProcess){

                    PasswordRecoveryProcess.Idle -> {

                        if (
                            email.email.isNotEmpty() &&
                            emailHint is SignInEmailHintViewState.Invisible

                        ) { ButtonProceedViewState.Enabled }

                        else ButtonProceedViewState.Disabled
                    }

                    PasswordRecoveryProcess.Pending -> ButtonProceedViewState.Pending

                    PasswordRecoveryProcess.Successful -> ButtonProceedViewState.Successful

                    is PasswordRecoveryProcess.Unsuccessful -> ButtonProceedViewState.Unsuccessful
                }

            _stateOfButtonProceed.emit(state)
        }.collect()
    }

    private suspend fun updateStateOfHint() {

        authenticationManager.passwordRecoveryProcess.collect {

            when(it) {

                is PasswordRecoveryProcess.Unsuccessful -> {

                    when(it.cause) {

                        UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat -> {

                            _stateOfEmailHint.emit(
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.InvalidEmailFormat
                                )
                            )
                        }

                        UnsuccessfulPasswordRecoveryCause.Timeout -> {

                            _stateOfEmailHint.emit(
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.Timeout
                                )
                            )
                        }

                        UnsuccessfulPasswordRecoveryCause.UnidentifiedException -> {

                            _stateOfEmailHint.emit(
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.UnidentifiedException
                                )
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun hideEmailHint() {

        if (_stateOfEmailHint.value is SignInEmailHintViewState.Visible) {
            _stateOfEmailHint.value = SignInEmailHintViewState.Invisible
        }
    }
}