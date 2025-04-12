package com.example.routesignedout

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation.Navigation

fun NavGraphBuilder.routeSignedOutImpl() {

    navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

        composable<Navigation.SignedOut.WelcomeScreen> { welcomeScreen() }
    }
}