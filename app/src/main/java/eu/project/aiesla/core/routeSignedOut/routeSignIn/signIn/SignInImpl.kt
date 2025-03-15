package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signInImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager,
) {

    composable<Navigation.SignedOut.SignIn.SignInScreen> {

        val signInProcess by
            authenticationManager
            .signInProcess
            .collectAsStateWithLifecycle()

        LaunchedEffect(signInProcess) {

            if (signInProcess is SignInProcess.Successful) {

                navHostController.navigate(
                    route = Navigation.SignedIn.RouteSignedIn,
                    builder = {
                        this.popUpTo(
                            route = Navigation.SignedOut.RouteSignedOut,
                            popUpToBuilder = { inclusive = true }
                        )
                    }
                )
            }
        }

        signInScreen(
            onSignIn = {

                authenticationManager.signIn(it)
            },
            onRecoverPassword = {

                navHostController.navigate(
                    route = Navigation.SignedOut.SignIn.RecoverYourPasswordScreen
                )
            }
        )
    }
}