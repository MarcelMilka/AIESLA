package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpEmailInformationImpl() {

    composable<Navigation.SignedOut.SignUp.SignUpEmailInformationScreen> {

        signUpEmailInformationScreen()
    }
}