package eu.project.aiesla.core.routeSignedOut.checkYourEmail.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.checkYourEmail.ui.passwordRecoveryEmailInformationScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.passwordRecoveryEmailInformationImpl() {

    composable<Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen> {

        passwordRecoveryEmailInformationScreen()
    }
}