package eu.project.aiesla.core.screenDock.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.project.aiesla.core.screenDock.ui.screenDock
import eu.project.aiesla.core.screenDock.vm.ScreenDockViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation
import eu.project.aiesla.sharedConstants.navigation.getCurrentMainRoute
import eu.project.aiesla.sharedConstants.navigation.getCurrentScreen

@Composable
fun screenDockImpl(
    navHostController: NavHostController
) {

    val screenDockViewModel = hiltViewModel<ScreenDockViewModel>()
    val screenDockViewState by screenDockViewModel.screenDockViewState.collectAsState()

    navHostController.addOnDestinationChangedListener { _, destination, _ ->

        screenDockViewModel.updateViewState(
            currentMainRoute = destination.getCurrentMainRoute(),
            currentScreen = destination.getCurrentScreen()
        )
    }

    screenDock(
        screenDockViewState = screenDockViewState,
        onNavigatePodcastsScreen = {},
        onNavigateHomeScreen = {

            navHostController.navigate(Navigation.SignedIn.Home.RouteHome)
        },
        onNavigateStudyScreen = {

            navHostController.navigate(Navigation.SignedIn.Study.RouteStudy)
        }
    )
}