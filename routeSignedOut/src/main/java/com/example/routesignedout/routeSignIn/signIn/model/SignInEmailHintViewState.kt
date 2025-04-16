package com.example.routesignedout.routeSignIn.signIn.model

internal sealed class SignInEmailHintViewState {

    data object Invisible: SignInEmailHintViewState()
    data class Visible(val hint: SignInEmailHintOptions): SignInEmailHintViewState()
}