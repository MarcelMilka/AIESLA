package eu.project.aiesla.auth.results

sealed class PasswordRecoveryProcess {

    data object Idle: PasswordRecoveryProcess()
    data object Pending: PasswordRecoveryProcess()
    data object Successful: PasswordRecoveryProcess()
    data class Unsuccessful(val cause: String): PasswordRecoveryProcess()
}

enum class ResultOfPasswordRecoveryProcess {
    Ok,
    UnidentifiedException,
}

object UnsuccessfulPasswordRecoveryCause {

    const val EXEMPLARY_CAUSE = "exemplary cause"
}