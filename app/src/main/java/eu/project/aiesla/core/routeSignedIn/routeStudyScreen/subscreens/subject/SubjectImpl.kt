package eu.project.aiesla.core.routeSignedIn.routeStudyScreen.subscreens.subject

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedIn.routeStudyScreen.vm.StudyViewModel
import eu.project.aiesla.core.sharedViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.subjectImpl(navHostController: NavHostController) {

    composable<Navigation.SignedIn.Study.SubjectSubscreen> {

        val studyViewModel = it.sharedViewModel<StudyViewModel>(navHostController)

        subjectSubscreen()
    }
}