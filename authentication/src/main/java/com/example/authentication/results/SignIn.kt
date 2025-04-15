package com.example.authentication.results

sealed class SignInProcess {

    data object Idle: SignInProcess()
    data object Pending: SignInProcess()
    data object Successful: SignInProcess()
    data class Unsuccessful(val cause: UnsuccessfulSignInProcessCause): SignInProcess()
}

enum class ResultOfSignInProcess {
    Ok,
    InvalidEmailFormat,
    PasswordIsIncorrect,
    UnidentifiedException,
}

enum class UnsuccessfulSignInProcessCause {
    InvalidEmailFormat,
    PasswordIsIncorrect,
    Timeout,
    UnidentifiedException,
}