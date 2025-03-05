package eu.project.aiesla.core.routeSignedIn.studyScreen.subscreens.subjects.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.studyScreen.subscreens.subjects.ui.subjectsSubscreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.subjectsImpl() {

    composable<Navigation.SignedIn.Study.SubjectsSubscreen> {

        subjectsSubscreen()
    }
}