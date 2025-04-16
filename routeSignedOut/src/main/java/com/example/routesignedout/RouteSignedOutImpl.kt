package com.example.routesignedout

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignIn.recoverYourPassword.impl.recoverYourPasswordImpl
import com.example.routesignedout.routeSignIn.signIn.impl.signInScreenImpl
import com.example.routesignedout.routeSignUp.signUp.impl.signUpScreenImpl
import com.example.routesignedout.routeSignUp.signUpEmailInformation.impl.signUpEmailInformationScreenImpl
import com.example.routesignedout.welcomeScreen.welcomeScreenImpl

fun NavGraphBuilder.routeSignedOutImpl(navigationController: NavHostController) {

    navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

        this.welcomeScreenImpl(navigationController)

        this.navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

            this.signUpScreenImpl(navigationController)

            this.signUpEmailInformationScreenImpl(navigationController)
        }

        this.navigation<Navigation.SignedOut.SignIn.RouteSignIn>(startDestination = Navigation.SignedOut.SignIn.SignInScreen) {

            this.signInScreenImpl(navigationController)

            this.recoverYourPasswordImpl(navigationController)
        }
    }
}