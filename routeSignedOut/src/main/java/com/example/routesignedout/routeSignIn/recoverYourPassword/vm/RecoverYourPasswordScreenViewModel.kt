package com.example.routesignedout.routeSignIn.recoverYourPassword.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.PasswordRecoveryProcess
import com.example.authentication.results.UnsuccessfulPasswordRecoveryCause
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintOptions
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintViewState
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecoverYourPasswordScreenViewModel @Inject constructor(
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