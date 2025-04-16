package com.example.routesignedout.routeSignIn.signIn.model

internal sealed class SignInPasswordHintViewState {

    data object Invisible: SignInPasswordHintViewState()
    data class Visible(val hint: SignInPasswordHintOptions): SignInPasswordHintViewState()
}