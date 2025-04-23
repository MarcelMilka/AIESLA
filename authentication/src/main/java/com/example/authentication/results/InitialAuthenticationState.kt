package com.example.authentication.results

sealed class InitialAuthenticationState {

    data object SignedIn: InitialAuthenticationState()

    data object SignedOut: InitialAuthenticationState()

    data class Unsuccessful(val cause: UnsuccessfulInitializationCause): InitialAuthenticationState()
}

enum class UnsuccessfulInitializationCause {
    Null,
    Timeout,
    UnidentifiedException
}