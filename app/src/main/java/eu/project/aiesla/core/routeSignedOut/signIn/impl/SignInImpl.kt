package eu.project.aiesla.core.routeSignedOut.signIn.impl

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.core.routeSignedOut.signIn.ui.signInScreen
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

            when (signInProcess) {
                SignInProcess.Idle -> {Log.d("Halla!", "Idle")}
                SignInProcess.Pending -> {Log.d("Halla!", "Pending")}
                SignInProcess.Successful -> {

                    Log.d("Halla!", "Successful")

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
                is SignInProcess.Unsuccessful -> {Log.d("Halla!", "Unsuccessful")}
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