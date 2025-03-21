package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation.ui.signUpEmailInformationScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpEmailInformationImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignUp.SignUpEmailInformationScreen> (

        enterTransition = { EnterTransition.None},

        exitTransition = { ExitTransition.None},

        content = {

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
    )
}