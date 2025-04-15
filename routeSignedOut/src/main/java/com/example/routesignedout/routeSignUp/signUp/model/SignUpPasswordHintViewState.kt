package com.example.routesignedout.routeSignUp.signUp.model

sealed class SignUpPasswordHintViewState {

    data object Invisible: SignUpPasswordHintViewState()
    data class Visible(
        val totalCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val uppercaseCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val specialCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val numericCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0)
    ): SignUpPasswordHintViewState()
}