package eu.project.aiesla.sharedConstants.navigation

import kotlinx.serialization.Serializable

// This class serves as the only way of navigating between screens.
sealed class Navigation {

    sealed class SignedOut: Navigation() {

        @Serializable
        data object RouteSignedOut: SignedOut()

        @Serializable
        data object WelcomeScreen: SignedOut()

        sealed class SignIn: SignedOut() {

            @Serializable
            data object RouteSignIn: SignIn()

            @Serializable
            data object SignInScreen: SignIn()

            @Serializable
            data object RecoverYourPasswordScreen: SignIn()

            @Serializable
            data object PasswordRecoveryEmailInformationScreen: SignIn()
        }

        sealed class SignUp: SignedOut() {

            @Serializable
            data object RouteSignUp: SignUp()

            @Serializable
            data object SignUpScreen: SignUp()

            @Serializable
            data object SignUpEmailInformationScreen: SignUp()
        }
    }

    sealed class SignedIn: Navigation() {

        @Serializable
        data object RouteSignedIn: SignedIn()

        sealed class Home: SignedIn() {

            @Serializable
            data object RouteHomeScreen: Home()

            @Serializable
            data object HomeSubscreen: Home()

//             @Serializable
//             data object ___Subscreen: Study()
        }

        sealed class Study: SignedIn() {

            @Serializable
            data object RouteStudyScreen: Study()

            @Serializable
            data object SubjectsSubscreen: Study()

            @Serializable
            data object SubjectSubscreen: Study()
        }
    }
}