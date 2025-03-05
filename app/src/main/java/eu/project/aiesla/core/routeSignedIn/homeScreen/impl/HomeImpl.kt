package eu.project.aiesla.core.routeSignedIn.homeScreen.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.homeScreen.ui.homeScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.homeImpl() {

    composable<Navigation.SignedIn.Home.HomeScreen> {

        homeScreen()
    }
}