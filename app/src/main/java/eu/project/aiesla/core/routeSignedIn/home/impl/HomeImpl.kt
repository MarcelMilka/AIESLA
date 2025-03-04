package eu.project.aiesla.core.routeSignedIn.home.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.home.ui.homeScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.homeImpl() {

    composable<Navigation.SignedIn.Home.HomeScreen> {

        homeScreen()
    }
}