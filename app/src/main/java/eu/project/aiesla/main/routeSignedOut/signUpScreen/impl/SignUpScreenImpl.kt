package eu.project.aiesla.main.routeSignedOut.signUpScreen.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.Credentials
import eu.project.aiesla.main.routeSignedOut.signUpScreen.ui.signUpScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpScreenImpl(
    navHostController: NavHostController,
    onSignUp: (Credentials) -> Unit
) {

    composable<Navigation.SignedOut.SignUp.SignUpScreen> {

        signUpScreen(
            onSignUp = { onSignUp(it) },
        )
    }
}