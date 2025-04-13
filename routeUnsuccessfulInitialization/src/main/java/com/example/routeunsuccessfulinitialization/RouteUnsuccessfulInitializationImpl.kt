package com.example.routeunsuccessfulinitialization

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.navigation.Navigation

fun NavGraphBuilder.routeUnsuccessfulInitializationImpl() {

    navigation<Navigation.UnsuccessfulAuthentication.RouteUnsuccessfulAuthentication>(startDestination = Navigation.UnsuccessfulAuthentication.UnsuccessfulAuthenticationScreen) {

        composable<Navigation.UnsuccessfulAuthentication.UnsuccessfulAuthenticationScreen> { unsuccessfulInitializationScreen() }
    }
}