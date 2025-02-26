package eu.project.aiesla.sharedConstants.navigation

import kotlinx.serialization.Serializable

// This class serves as the only way of navigating between screens.
sealed class Navigation {

    sealed class SignedOut: Navigation() {

        @Serializable
        data object RouteSignedOut: SignedOut()

        @Serializable
        data object WelcomeScreen: SignedOut()

        sealed class SignUp: SignedOut() {

            @Serializable
            data object RouteSignUp: SignUp()

            @Serializable
            data object SignUpScreen: SignUp()

            @Serializable
            data object VerifyYourEmailScreen: SignUp()
        }

        sealed class SignIn: SignedOut() {

            @Serializable
            data object RouteSignIn: SignUp()

            @Serializable
            data object SignInScreen: SignUp()

            @Serializable
            data object RecoverYourPasswordScreen: SignUp()

            @Serializable
            data object CheckYourEmailScreen: SignUp()
        }
    }

    sealed class SignedIn: Navigation() {

        @Serializable
        data object RouteSignedIn: SignedIn()

        @Serializable
        data object PodcastsScreen: SignedIn()
    }
}