package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.SignUpProcess
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignUp.SignUpScreen>(

        enterTransition = { EnterTransition.None},

        exitTransition = { ExitTransition.None},

        content = {

            val signUpProcess by
            authenticationManager
                .signUpProcess
                .collectAsStateWithLifecycle()

            LaunchedEffect(signUpProcess) {

                if (signUpProcess is SignUpProcess.Successful) {

                    navHostController.navigate(
                        route = Navigation.SignedOut.SignUp.SignUpEmailInformationScreen,
                        builder = {
                            this.popUpTo(
                                route = Navigation.SignedOut.WelcomeScreen,
                                popUpToBuilder = { inclusive = false }
                            )
                        }
                    )
                }
            }

            signUpScreen(

                signUpProcess = signUpProcess,

                onSignUp = {

                    authenticationManager.signUp(
                        credentials = it
                    )
                },
            )
        }
    )
}