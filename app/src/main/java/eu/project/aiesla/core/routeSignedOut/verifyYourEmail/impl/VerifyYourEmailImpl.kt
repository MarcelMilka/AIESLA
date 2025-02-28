package eu.project.aiesla.core.routeSignedOut.verifyYourEmail.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.verifyYourEmail.ui.verifyYourEmailScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.verifyYourEmailImpl() {

    composable<Navigation.SignedOut.SignUp.VerifyYourEmailScreen> {

        verifyYourEmailScreen()
    }
}