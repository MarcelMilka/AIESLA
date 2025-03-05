package eu.project.aiesla.core.routeSignedOut.routeSignIn.passwordRecoveryEmailInformation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.passwordRecoveryEmailInformationImpl() {

    composable<Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen> {

        passwordRecoveryEmailInformationScreen()
    }
}