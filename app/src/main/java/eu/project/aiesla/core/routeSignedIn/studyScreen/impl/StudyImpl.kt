package eu.project.aiesla.core.routeSignedIn.studyScreen.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.studyScreen.ui.studyScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.studyImpl() {

    composable<Navigation.SignedIn.Study.StudyScreen> {

        studyScreen()
    }
}