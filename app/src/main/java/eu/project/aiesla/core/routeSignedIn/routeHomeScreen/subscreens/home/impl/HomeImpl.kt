package eu.project.aiesla.core.routeSignedIn.routeHomeScreen.subscreens.home.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.routeHomeScreen.subscreens.home.ui.homeSubscreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.homeImpl() {

    composable<Navigation.SignedIn.Home.HomeSubscreen> {

        homeSubscreen()
    }
}