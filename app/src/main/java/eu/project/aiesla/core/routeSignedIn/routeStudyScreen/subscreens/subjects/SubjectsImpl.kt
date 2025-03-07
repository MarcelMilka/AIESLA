package eu.project.aiesla.core.routeSignedIn.routeStudyScreen.subscreens.subjects

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.routeStudyScreen.vm.StudyViewModel
import eu.project.aiesla.core.sharedViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.subjectsImpl(navHostController: NavHostController) {

    composable<Navigation.SignedIn.Study.SubjectsSubscreen> {

        val studyViewModel = it.sharedViewModel<StudyViewModel>(navHostController)

        subjectsSubscreen()
    }
}