package eu.project.aiesla.auth.results

sealed class SignUpProcess {

    data object Idle: SignUpProcess()
    data object Pending: SignUpProcess()
    data object Successful: SignUpProcess()
    data class Unsuccessful(val cause: String): SignUpProcess()
}

enum class ResultOfSignUpProcess {
    Ok,
    UnidentifiedException,
}

object UnsuccessfulSignUpProcessCause {

    const val EXEMPLARY_CAUSE = "exemplary cause"
}