package eu.project.aiesla.auth.results

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
    UnidentifiedException,
}