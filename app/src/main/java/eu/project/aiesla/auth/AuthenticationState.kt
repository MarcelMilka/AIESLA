package eu.project.aiesla.auth

sealed class AuthenticationState {

    data object Idle: AuthenticationState()

    data object SignedOut: AuthenticationState()

    data object SigningUp: AuthenticationState()
    data object FailedToSignUp: AuthenticationState()

    data object WaitingForVerification: AuthenticationState()
    data object FailedToSendVerificationEmail: AuthenticationState()

    data object SignedIn: AuthenticationState()
}