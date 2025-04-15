package com.example.authentication.results

sealed class SignUpProcess {

    data object Idle: SignUpProcess()
    data object Pending: SignUpProcess()
    data object Successful: SignUpProcess()
    data class Unsuccessful(val cause: UnsuccessfulSignUpProcessCause): SignUpProcess()
}

enum class ResultOfSignUpProcess {
    Ok,
    InvalidEmailFormat,
    EmailIsAlreadyInUse,
    UnidentifiedException,
}

enum class UnsuccessfulSignUpProcessCause {
    InvalidEmailFormat,
    EmailIsAlreadyInUse,
    Timeout,
    UnidentifiedException,
}