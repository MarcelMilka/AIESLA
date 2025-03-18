package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.credentials.PasswordCredential
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.auth.results.UnsuccessfulSignInProcessCause
import eu.project.aiesla.sharedUi.sharedElements.button.ButtonProceedViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.EmailTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.EmailTextFieldViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.PasswordTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.PasswordTextFieldViewState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    val signInProcess = this.authenticationManager.signInProcess

    private val _credentials = MutableStateFlow(EmailAndPasswordCredentials("", ""))
    val credentials = _credentials.asStateFlow()

    private val _stateOfEmailHint = MutableStateFlow<EmailTextFieldViewState>(EmailTextFieldViewState.Invisible)
    val stateOfEmailHint = _stateOfEmailHint.asStateFlow()

    private val _stateOfPasswordHint = MutableStateFlow<PasswordTextFieldViewState>(PasswordTextFieldViewState.Invisible)
    val stateOfPasswordHint = _stateOfPasswordHint.asStateFlow()

    private val _stateOfButtonProceed = MutableStateFlow<ButtonProceedViewState>(ButtonProceedViewState.Disabled)
    val stateOfButtonProceed = _stateOfButtonProceed.asStateFlow()



    init {

        viewModelScope.launch { authenticationManager.resetProcesses() }

        viewModelScope.launch { updateStateOfTheButtonProceed() }

        viewModelScope.launch { updateStatesOfHints() }
    }

    fun updateEmail(email: EmailCredential) {

        _credentials.update { it.copy(email = email.email) }
        authenticationManager.resetProcesses()
        hideEmailHint()
    }

    fun updatePassword(password: PasswordCredential) {

        _credentials.update { it.copy(password = password.password) }
        authenticationManager.resetProcesses()
        hidePasswordHint()
    }

    fun getCredentials(): EmailAndPasswordCredentials = _credentials.value

    fun signIn() {

        val credentials = this.getCredentials()
        authenticationManager.signIn(credentials = credentials)
    }


    // 'state operators'
    private suspend fun updateStateOfTheButtonProceed() {

        combine(
            _credentials,
            authenticationManager.signInProcess,
            _stateOfEmailHint,
            _stateOfPasswordHint
        ) { credentials, signInProcess, emailHint, passwordHint ->

            val state =
                when(signInProcess){

                    SignInProcess.Idle -> {

                        if (
                            credentials.email.isNotEmpty() &&
                            credentials.password.isNotEmpty() &&
                            emailHint is EmailTextFieldViewState.Invisible &&
                            passwordHint is PasswordTextFieldViewState.Invisible

                        ) { ButtonProceedViewState.Enabled }

                        else ButtonProceedViewState.Disabled
                    }

                    SignInProcess.Pending -> ButtonProceedViewState.Pending

                    SignInProcess.Successful -> ButtonProceedViewState.Successful

                    is SignInProcess.Unsuccessful -> ButtonProceedViewState.Unsuccessful
                }

            _stateOfButtonProceed.emit(state)
        }.collect()
    }

    private suspend fun updateStatesOfHints() {

        authenticationManager.signInProcess.collect {

            when(it) {

                is SignInProcess.Unsuccessful -> {

                    when(it.cause) {

                        UnsuccessfulSignInProcessCause.InvalidEmailFormat -> {

                            _stateOfEmailHint.emit(
                                value = EmailTextFieldViewState.Visible(
                                    hint = EmailTextFieldHint.InvalidEmailFormat
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.PasswordIsIncorrect -> {

                            _stateOfPasswordHint.emit(
                                value = PasswordTextFieldViewState.Visible(
                                    hint = PasswordTextFieldHint.PasswordIsIncorrect
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.Timeout -> {

                            _stateOfEmailHint.emit(
                                value = EmailTextFieldViewState.Visible(
                                    hint = EmailTextFieldHint.Timeout
                                )
                            )

                            _stateOfPasswordHint.emit(
                                value = PasswordTextFieldViewState.Visible(
                                    hint = PasswordTextFieldHint.Timeout
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.UnidentifiedException -> {

                            _stateOfEmailHint.emit(
                                value = EmailTextFieldViewState.Visible(
                                    hint = EmailTextFieldHint.UnidentifiedException
                                )
                            )

                            _stateOfPasswordHint.emit(
                                value = PasswordTextFieldViewState.Visible(
                                    hint = PasswordTextFieldHint.UnidentifiedException
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

        if (_stateOfEmailHint.value is EmailTextFieldViewState.Visible) {
            _stateOfEmailHint.value = EmailTextFieldViewState.Invisible
        }
    }

    private fun hidePasswordHint() {

        if (_stateOfPasswordHint.value is PasswordTextFieldViewState.Visible) {
            _stateOfPasswordHint.value = PasswordTextFieldViewState.Invisible
        }
    }
}