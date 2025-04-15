package com.example.routesignedout.routeSignUp.signUp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.credentials.PasswordCredential
import com.example.authentication.credentials.PasswordRequirements
import com.example.authentication.results.SignUpProcess
import com.example.authentication.results.UnsuccessfulSignUpProcessCause
import com.example.routesignedout.routeSignUp.signUp.model.PasswordRequirementViewState
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintOptions
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintViewState
import com.example.routesignedout.routeSignUp.signUp.model.SignUpPasswordHintViewState
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    val signUpProcess = this.authenticationManager.signUpProcess

    private val _credentials = MutableStateFlow(EmailAndPasswordCredentials("", ""))
    val credentials = _credentials.asStateFlow()

    private val _stateOfEmailHint = MutableStateFlow<SignUpEmailHintViewState>(SignUpEmailHintViewState.Invisible)
    val stateOfEmailHint = _stateOfEmailHint.asStateFlow()

    private val _stateOfPasswordHint = MutableStateFlow<SignUpPasswordHintViewState>(SignUpPasswordHintViewState.Invisible)
    val stateOfPasswordHint = _stateOfPasswordHint.asStateFlow()

    private val _stateOfButtonProceed = MutableStateFlow<ButtonProceedViewState>(ButtonProceedViewState.Disabled)
    val stateOfButtonProceed = _stateOfButtonProceed.asStateFlow()



    init {

        viewModelScope.launch { authenticationManager.resetProcesses() }

        viewModelScope.launch { updateStateOfEmailHint() }

        viewModelScope.launch { updateStateOfPasswordHint() }

        viewModelScope.launch { updateStateOfTheButtonProceed() }
    }

    fun updateEmail(email: EmailCredential) {

        _credentials.update { it.copy(email = email.email) }
        authenticationManager.resetProcesses()
        hideEmailHint()
    }

    fun updatePassword(password: PasswordCredential) {

        _credentials.update { it.copy(password = password.password) }
    }

    fun reactToOnFocusChanged(passwordTextFieldIsFocused: Boolean) {

        viewModelScope.launch {

            val stateOfPasswordHint =  when(passwordTextFieldIsFocused) {

                true -> SignUpPasswordHintViewState.Visible()

                false -> SignUpPasswordHintViewState.Invisible
            }

            _stateOfPasswordHint.emit(value = stateOfPasswordHint)
        }
    }

    private fun getCredentials(): EmailAndPasswordCredentials = _credentials.value

    fun signUp() {

        val credentials = this.getCredentials()
        authenticationManager.signUp(credentials = credentials)
    }



    // 'state operators'
    private suspend fun updateStateOfEmailHint() {

        authenticationManager.signUpProcess.collect {

            when(it) {

                is SignUpProcess.Unsuccessful -> {

                    val stateOfEmailHint = when(it.cause) {

                        UnsuccessfulSignUpProcessCause.InvalidEmailFormat -> {

                            SignUpEmailHintViewState.Visible(
                                hint = SignUpEmailHintOptions.InvalidEmailFormat
                            )
                        }

                        UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse -> {

                            SignUpEmailHintViewState.Visible(
                                hint = SignUpEmailHintOptions.EmailIsAlreadyInUse
                            )
                        }

                        UnsuccessfulSignUpProcessCause.Timeout -> {

                            SignUpEmailHintViewState.Visible(
                                hint = SignUpEmailHintOptions.Timeout
                            )
                        }

                        UnsuccessfulSignUpProcessCause.UnidentifiedException -> {

                            SignUpEmailHintViewState.Visible(
                                hint = SignUpEmailHintOptions.UnidentifiedException
                            )
                        }
                    }

                    _stateOfEmailHint.emit(value = stateOfEmailHint)

                }

                else -> {}
            }
        }
    }

    private fun hideEmailHint() {

        if (_stateOfEmailHint.value is SignUpEmailHintViewState.Visible) {
            _stateOfEmailHint.value = SignUpEmailHintViewState.Invisible
        }
    }

    private suspend fun updateStateOfPasswordHint() {

        _stateOfPasswordHint.combine(credentials) { stateOfPasswordHint, credentials ->

            when(stateOfPasswordHint) {

                is SignUpPasswordHintViewState.Visible -> {

                    val totalCharacters = credentials.password.count()
                    val uppercaseCharacters = credentials.password.count { it.isUpperCase() }
                    val specialCharacters = credentials.password.count { !it.isLetterOrDigit() }
                    val numericCharacters = credentials.password.count { it.isDigit() }

                    val viewState = SignUpPasswordHintViewState.Visible(

                        totalCharacters = PasswordRequirementViewState(
                            isGreen = totalCharacters >= PasswordRequirements.MIN_CHARACTERS_COUNT,
                            currentCount = totalCharacters
                        ),

                        uppercaseCharacters = PasswordRequirementViewState(
                            isGreen = uppercaseCharacters >= PasswordRequirements.MIN_UPPERCASE_COUNT,
                            currentCount = uppercaseCharacters
                        ),

                        specialCharacters = PasswordRequirementViewState(
                            isGreen = specialCharacters >= PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT,
                            currentCount = specialCharacters
                        ),

                        numericCharacters = PasswordRequirementViewState(
                            isGreen = numericCharacters >= PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT,
                            currentCount = numericCharacters
                        )
                    )

                    _stateOfPasswordHint.emit(viewState)
                }

                else -> {}
            }
        }.collect()
    }

    private suspend fun updateStateOfTheButtonProceed() {

        combine(
            _credentials,
            authenticationManager.signUpProcess,
            _stateOfEmailHint
        ) { credentials, signUpProcess, emailHint ->

            val state = when(signUpProcess) {

                SignUpProcess.Idle -> {

                    if (
                        credentials.email.isNotEmpty() &&
                        credentials.password.count() >= PasswordRequirements.MIN_CHARACTERS_COUNT &&
                        credentials.password.count { it.isUpperCase() } >= PasswordRequirements.MIN_UPPERCASE_COUNT &&
                        credentials.password.count { !it.isLetterOrDigit() } >= PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT &&
                        credentials.password.count { it.isDigit() } >= PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT &&
                        emailHint is SignUpEmailHintViewState.Invisible
                    ) { ButtonProceedViewState.Enabled }

                    else ButtonProceedViewState.Disabled
                }

                SignUpProcess.Pending -> ButtonProceedViewState.Pending

                SignUpProcess.Successful -> ButtonProceedViewState.Successful

                is SignUpProcess.Unsuccessful -> ButtonProceedViewState.Unsuccessful
            }

            _stateOfButtonProceed.emit(value = state)

        }.collect()
    }
}