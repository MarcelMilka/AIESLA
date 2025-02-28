package eu.project.aiesla.auth.results

sealed class SignInProcess {

    data object Idle: SignInProcess()
    data object Pending: SignInProcess()
    data object Successful: SignInProcess()
    data class Unsuccessful(val cause: String): SignInProcess()
}

enum class ResultOfSignInProcess {
    Ok,
    UnidentifiedException
}

object UnsuccessfulSignInProcessCause {

    const val EXEMPLARY_CAUSE = "exemplary cause"
}