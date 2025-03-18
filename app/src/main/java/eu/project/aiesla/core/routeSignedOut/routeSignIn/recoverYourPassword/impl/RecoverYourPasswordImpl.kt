package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.impl

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
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.ui.recoverYourPasswordScreen
import eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.vm.RecoverYourPasswordScreenViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation
import kotlinx.coroutines.delay

fun NavGraphBuilder.recoverYourPasswordImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignIn.RecoverYourPasswordScreen>(

        enterTransition = { EnterTransition.None},

        exitTransition = { ExitTransition.None},

        content = {

            val recoverYourPasswordScreenViewModel = hiltViewModel<RecoverYourPasswordScreenViewModel>()

            val passwordRecoveryProcess by
            recoverYourPasswordScreenViewModel
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
                email = recoverYourPasswordScreenViewModel.email.collectAsStateWithLifecycle().value,

                onUpdateEmail = { recoverYourPasswordScreenViewModel.updateEmail(it) },

                emailHintViewState = recoverYourPasswordScreenViewModel.stateOfEmailHint.collectAsStateWithLifecycle().value,

                buttonProceedViewState = recoverYourPasswordScreenViewModel.stateOfButtonProceed.collectAsState().value,

                onRecoverPassword = { recoverYourPasswordScreenViewModel.sendPasswordRecoveryEmail() }
            )
        }
    )
}