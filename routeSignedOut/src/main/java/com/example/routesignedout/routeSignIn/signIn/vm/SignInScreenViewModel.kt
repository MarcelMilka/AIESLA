package com.example.routesignedout.routeSignIn.signIn.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.credentials.PasswordCredential
import com.example.authentication.results.SignInProcess
import com.example.authentication.results.UnsuccessfulSignInProcessCause
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintOptions
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintViewState
import com.example.routesignedout.routeSignIn.signIn.model.SignInPasswordHintOptions
import com.example.routesignedout.routeSignIn.signIn.model.SignInPasswordHintViewState
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SignInScreenViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    val signInProcess = this.authenticationManager.signInProcess

    private val _credentials = MutableStateFlow(EmailAndPasswordCredentials("", ""))
    val credentials = _credentials.asStateFlow()

    private val _stateOfEmailHint = MutableStateFlow<SignInEmailHintViewState>(SignInEmailHintViewState.Invisible)
    val stateOfEmailHint = _stateOfEmailHint.asStateFlow()

    private val _stateOfPasswordHint = MutableStateFlow<SignInPasswordHintViewState>(SignInPasswordHintViewState.Invisible)
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
                            emailHint is SignInEmailHintViewState.Invisible &&
                            passwordHint is SignInPasswordHintViewState.Invisible

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
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.InvalidEmailFormat
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.PasswordIsIncorrect -> {

                            _stateOfPasswordHint.emit(
                                value = SignInPasswordHintViewState.Visible(
                                    hint = SignInPasswordHintOptions.PasswordIsIncorrect
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.Timeout -> {

                            _stateOfEmailHint.emit(
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.Timeout
                                )
                            )

                            _stateOfPasswordHint.emit(
                                value = SignInPasswordHintViewState.Visible(
                                    hint = SignInPasswordHintOptions.Timeout
                                )
                            )
                        }

                        UnsuccessfulSignInProcessCause.UnidentifiedException -> {

                            _stateOfEmailHint.emit(
                                value = SignInEmailHintViewState.Visible(
                                    hint = SignInEmailHintOptions.UnidentifiedException
                                )
                            )

                            _stateOfPasswordHint.emit(
                                value = SignInPasswordHintViewState.Visible(
                                    hint = SignInPasswordHintOptions.UnidentifiedException
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

    private fun hidePasswordHint() {

        if (_stateOfPasswordHint.value is SignInPasswordHintViewState.Visible) {
            _stateOfPasswordHint.value = SignInPasswordHintViewState.Invisible
        }
    }
}