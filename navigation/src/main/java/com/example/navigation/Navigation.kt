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
        }

        sealed class SignUp: SignedOut() {

            @Serializable
            data object RouteSignUp: SignUp()

            @Serializable
            data object SignUpScreen: SignUp()
        }
    }

    sealed class SignedIn: Navigation() {

        @Serializable
        data object RouteSignedIn: SignedIn()

        sealed class Home: SignedIn() {

            @Serializable
            data object RouteHomeScreen: Home()

            @Serializable
            data object HomeScreen: Home()
        }
    }

    sealed class UnsuccessfulAuthentication: Navigation() {

        @Serializable
        data object UnsuccessfulAuthenticationScreen: UnsuccessfulAuthentication()
    }
}