package com.example.routesignedin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.navigation.Navigation

fun NavGraphBuilder.routeSignedInImpl() {

    navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.Home.HomeScreen) {

        composable<Navigation.SignedIn.Home.HomeScreen> { homeScreen() }
    }
}