package com.example.routesignedout.routeSignIn.signIn.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.authentication.results.SignInProcess
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignIn.signIn.ui.signInScreen
import com.example.routesignedout.routeSignIn.signIn.vm.SignInScreenViewModel
import kotlinx.coroutines.delay

internal fun NavGraphBuilder.signInScreenImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignIn.SignInScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            val viewModel = hiltViewModel<SignInScreenViewModel>()

            val signInProcess by
            viewModel
                .signInProcess
                .collectAsStateWithLifecycle()

            LaunchedEffect(signInProcess) {

                if (signInProcess is SignInProcess.Successful) {

                    delay(100)

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
                credentials = viewModel.credentials.collectAsStateWithLifecycle().value,

                onUpdateEmail = { viewModel.updateEmail(it) },

                onUpdatePassword = { viewModel.updatePassword(it) },

                emailHintViewState = viewModel.stateOfEmailHint.collectAsStateWithLifecycle().value,

                passwordHintViewState = viewModel.stateOfPasswordHint.collectAsStateWithLifecycle().value,

                buttonProceedViewState = viewModel.stateOfButtonProceed.collectAsState().value,

                onSignIn = { viewModel.signIn() },

                onRecoverPassword = {

                    navHostController.navigate(
                        route = Navigation.SignedOut.SignIn.RecoverYourPasswordScreen
                    )
                }
            )
        }
    )
}