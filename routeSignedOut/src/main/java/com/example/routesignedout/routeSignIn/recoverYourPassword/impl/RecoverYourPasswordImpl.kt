package com.example.routesignedout.routeSignIn.recoverYourPassword.impl

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
import com.example.authentication.results.PasswordRecoveryProcess
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignIn.recoverYourPassword.ui.recoverYourPasswordScreen
import com.example.routesignedout.routeSignIn.recoverYourPassword.vm.RecoverYourPasswordScreenViewModel
import kotlinx.coroutines.delay

internal fun NavGraphBuilder.recoverYourPasswordImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignIn.RecoverYourPasswordScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            val viewModel = hiltViewModel<RecoverYourPasswordScreenViewModel>()

            val passwordRecoveryProcess by
            viewModel
                .signInProcess
                .collectAsStateWithLifecycle()

            LaunchedEffect(passwordRecoveryProcess) {

                if (passwordRecoveryProcess is PasswordRecoveryProcess.Successful) {

                    delay(100)

                    navHostController.navigate(
                        route = Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen,
                        builder = {
                            this.popUpTo(
                                route = Navigation.SignedOut.WelcomeScreen,
                                popUpToBuilder = { inclusive = false }
                            )
                        }
                    )
                }
            }

            recoverYourPasswordScreen(
                email = viewModel.email.collectAsStateWithLifecycle().value,

                onUpdateEmail = { viewModel.updateEmail(it) },

                emailHintViewState = viewModel.stateOfEmailHint.collectAsStateWithLifecycle().value,

                buttonProceedViewState = viewModel.stateOfButtonProceed.collectAsState().value,

                onRecoverPassword = { viewModel.sendPasswordRecoveryEmail() }
            )
        }
    )
}