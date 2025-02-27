package eu.project.aiesla.main.routeSignedOut.signIn.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.AuthenticationManager
import eu.project.aiesla.main.routeSignedOut.signIn.ui.signInScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signInImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignIn.SignInScreen> {

        signInScreen(
            onSignIn = {

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