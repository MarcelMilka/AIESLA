package eu.project.aiesla.core.routeSignedOut.signUpEmailInformation.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.signUpEmailInformation.ui.signUpEmailInformationScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpEmailInformationImpl() {

    composable<Navigation.SignedOut.SignUp.SignUpEmailInformationScreen> {

        signUpEmailInformationScreen()
    }
}