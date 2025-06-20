package com.example.navigation

import kotlinx.serialization.Serializable

// The following class serves as a single source of truth to navigate in the application.

//routeUnsuccessfulInitialisation

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
            data object RouteHome: Home()

            @Serializable
            data object HomeScreen: Home()
        }

        sealed class Study: SignedIn() {

            @Serializable
            data object RouteStudy: Home()

            @Serializable
            data object SubjectsScreen: Home()
        }
    }

    sealed class UnsuccessfulAuthentication: Navigation() {

        @Serializable
        data object RouteUnsuccessfulAuthentication: UnsuccessfulAuthentication()

        @Serializable
        data object UnsuccessfulAuthenticationScreen: UnsuccessfulAuthentication()
    }
}