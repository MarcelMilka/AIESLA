package eu.project.aiesla.main.routeSignedOut.checkYourEmail.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.main.routeSignedOut.checkYourEmail.ui.checkYourEmailScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.checkYourEmailImpl() {

    composable<Navigation.SignedOut.SignIn.CheckYourEmailScreen> {

        checkYourEmailScreen()
    }
}