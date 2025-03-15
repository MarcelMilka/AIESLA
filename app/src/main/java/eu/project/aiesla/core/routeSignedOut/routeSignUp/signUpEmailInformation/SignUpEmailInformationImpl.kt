package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpEmailInformationImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignUp.SignUpEmailInformationScreen> {

        signUpEmailInformationScreen(
            onSignIn = {

                navHostController.navigate(
                    route = Navigation.SignedOut.SignIn.RouteSignIn,
                    builder = {
                        this.popUpTo(
                            route = Navigation.SignedOut.WelcomeScreen,
                            popUpToBuilder = { inclusive = false }
                        )
                    }
                )
            }
        )
    }
}