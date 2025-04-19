package com.example.routesignedin.routeStudy

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation

internal fun NavGraphBuilder.subjectsScreenImpl(navigationController: NavHostController) {

    composable<Navigation.SignedIn.Study.SubjectsScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            subjectsScreen()
        }
    )
}