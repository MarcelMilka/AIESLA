package com.example.routesignedout.routeSignUp.signUp.model

sealed class SignUpEmailHintViewState {

    data object Invisible: SignUpEmailHintViewState()
    data class Visible(val hint: SignUpEmailHintOptions): SignUpEmailHintViewState()
}