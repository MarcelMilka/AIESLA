package eu.project.aiesla.sharedConstants.navigation

import kotlinx.serialization.Serializable

// This class serves as the only way of navigating between screens.
sealed class Navigation {

    sealed class SignedOut: Navigation() {

        @Serializable
        data object RouteSignedOut: SignedOut()

        @Serializable
        data object WelcomeScreen: SignedOut()

        sealed class RouteSignUp: SignedOut() {

            @Serializable
            data object SignUpScreen: RouteSignUp()

            @Serializable
            data object SignUpWitEmailAndPasswordScreen: RouteSignUp()

            @Serializable
            data object ConfirmYourRegistrationScreen: RouteSignUp()

        }
    }


    sealed class SignedIn: Navigation() {

        @Serializable
        data object RouteSignedIn: SignedIn()

        @Serializable
        data object PodcastsScreen: SignedIn()
    }
}