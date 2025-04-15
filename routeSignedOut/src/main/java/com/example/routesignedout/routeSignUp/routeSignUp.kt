package com.example.routesignedout.routeSignUp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignUp.signUp.impl.signUpScreenImpl
import com.example.routesignedout.welcomeScreen.welcomeScreenImpl

fun NavGraphBuilder.routeSignUp(navigationController: NavHostController) {

    navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

        this.welcomeScreenImpl(navigationController)

        this.signUpScreenImpl(navigationController)
    }
}