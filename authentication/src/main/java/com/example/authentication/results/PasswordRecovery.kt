package com.example.authentication.results

sealed class PasswordRecoveryProcess {

    data object Idle: PasswordRecoveryProcess()
    data object Pending: PasswordRecoveryProcess()
    data object Successful: PasswordRecoveryProcess()
    data class Unsuccessful(val cause: UnsuccessfulPasswordRecoveryCause): PasswordRecoveryProcess()
}

enum class ResultOfPasswordRecoveryProcess {
    Ok,
    InvalidEmailFormat,
    UnidentifiedException,
}

enum class UnsuccessfulPasswordRecoveryCause {
    InvalidEmailFormat,
    Timeout,
    UnidentifiedException,
}