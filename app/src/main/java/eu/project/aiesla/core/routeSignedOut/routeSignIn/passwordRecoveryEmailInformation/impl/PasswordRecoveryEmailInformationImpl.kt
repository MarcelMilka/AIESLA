package eu.project.aiesla.core.routeSignedOut.routeSignIn.passwordRecoveryEmailInformation.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.routeSignedOut.routeSignIn.passwordRecoveryEmailInformation.ui.passwordRecoveryEmailInformationScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.passwordRecoveryEmailInformationImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen> (

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            LaunchedEffect(true) { authenticationManager.resetProcesses() }

            passwordRecoveryEmailInformationScreen(
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