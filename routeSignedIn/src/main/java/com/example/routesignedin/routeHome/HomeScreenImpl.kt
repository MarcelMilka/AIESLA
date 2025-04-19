package com.example.routesignedin.routeHome

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation

internal fun NavGraphBuilder.homeScreenImpl(navigationController: NavHostController) {

    composable<Navigation.SignedIn.Home.HomeScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            homeScreen()
        }
    )
}