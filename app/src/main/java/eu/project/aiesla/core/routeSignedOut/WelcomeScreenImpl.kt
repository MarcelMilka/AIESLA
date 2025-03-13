package eu.project.aiesla.core.routeSignedOut

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.welcomeScreenImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.WelcomeScreen>(

        enterTransition = {EnterTransition.None},

        exitTransition = {ExitTransition.None},

        content = {

            welcomeScreen(
                onSignIn = {

                    navHostController.navigate(route = Navigation.SignedOut.SignIn.RouteSignIn)
                },

                onSignUp = {

                    navHostController.navigate(route = Navigation.SignedOut.SignUp.RouteSignUp)
                }
            )
        }
    )
}