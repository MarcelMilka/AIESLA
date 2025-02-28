package eu.project.aiesla.core.routeSignedOut.welcomeScreen.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.core.routeSignedOut.welcomeScreen.ui.welcomeScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.welcomeScreenImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.WelcomeScreen> {

        welcomeScreen(
            onSignIn = {

                navHostController.navigate(route = Navigation.SignedOut.SignIn.RouteSignIn)
            },

            onSignUp = {

                navHostController.navigate(route = Navigation.SignedOut.SignUp.RouteSignUp)
            },

            onContinueWithoutAccount = {

                navHostController.navigate(route = Navigation.SignedIn.RouteSignedIn)
            }
        )
    }
}