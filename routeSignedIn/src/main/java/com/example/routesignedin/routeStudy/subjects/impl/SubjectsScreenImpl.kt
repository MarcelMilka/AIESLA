package com.example.routesignedin.routeStudy.subjects.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation
import com.example.routesignedin.routeStudy.subjects.ui.subjectsScreen
import com.example.routesignedin.routeStudy.subjects.vm.SharedStudyViewModel

internal fun NavGraphBuilder.subjectsScreenImpl(navigationController: NavHostController) {

    composable<Navigation.SignedIn.Study.SubjectsScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            val sharedStudyViewModel = hiltViewModel<SharedStudyViewModel>()

            subjectsScreen()
        }
    )
}