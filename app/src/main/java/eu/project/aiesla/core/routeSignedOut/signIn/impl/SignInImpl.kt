package eu.project.aiesla.core.routeSignedOut.signIn.impl

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.AuthenticationManager
import eu.project.aiesla.core.routeSignedOut.signIn.ui.signInScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signInImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignIn.SignInScreen> {

        signInScreen(
            onSignIn = {

                authenticationManager.signIn(it)

                navHostController.navigate(
                    route = Navigation.SignedIn.RouteSignedIn,
                    builder = {
                        this.popUpTo(
                            route = Navigation.SignedOut.RouteSignedOut,
                            popUpToBuilder = { inclusive = true }
                        )
                    }
                )
            },
            onRecoverPassword = {

                navHostController.navigate(
                    route = Navigation.SignedOut.SignIn.RecoverYourPasswordScreen
                )
            }
        )
    }
}