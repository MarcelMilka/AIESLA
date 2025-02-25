package eu.project.aiesla.main.routeSignedOut.signInScreen.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.Credentials
import eu.project.aiesla.main.routeSignedOut.signInScreen.ui.signInScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signInScreenImpl(
    navHostController: NavHostController,
    onSignIn: (Credentials) -> Unit
) {

    composable<Navigation.SignedOut.SignIn.SignInScreen> {

        signInScreen { onSignIn(it) }
    }
}