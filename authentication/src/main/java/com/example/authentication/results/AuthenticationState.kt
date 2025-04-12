package com.example.authentication.results

sealed class AuthenticationState {

    data object SignedIn: AuthenticationState()

    data object SignedOut: AuthenticationState()

    data class Unsuccessful(val cause: UnsuccessfulInitializationCause): AuthenticationState()
}

enum class UnsuccessfulInitializationCause {
    Null,
    Timeout,
    UnidentifiedException
}