package eu.project.aiesla.core.routeSignedOut.checkYourEmail.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.checkYourEmail.ui.checkYourEmailScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.checkYourEmailImpl() {

    composable<Navigation.SignedOut.SignIn.CheckYourEmailScreen> {

        checkYourEmailScreen()
    }
}