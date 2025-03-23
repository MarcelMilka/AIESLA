package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.results.SignUpProcess
import eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp.ui.signUpScreen
import eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp.vm.SignUpScreenViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation
import kotlinx.coroutines.delay

fun NavGraphBuilder.signUpImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignUp.SignUpScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            val viewModel = hiltViewModel<SignUpScreenViewModel>()

            val signUpProcess by
                viewModel
                .signUpProcess
                .collectAsStateWithLifecycle()

            LaunchedEffect(signUpProcess) {

                delay(100)

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
                credentials = viewModel.credentials.collectAsStateWithLifecycle().value,

                onUpdateEmail = { viewModel.updateEmail(it) },

                onUpdatePassword = { viewModel.updatePassword(it) },

                onFocusChanged = { viewModel.reactToOnFocusChanged(it) },

                emailHintViewState = viewModel.stateOfEmailHint.collectAsStateWithLifecycle().value,

                passwordHintViewState = viewModel.stateOfPasswordHint.collectAsStateWithLifecycle().value,

                buttonProceedViewState = viewModel.stateOfButtonProceed.collectAsStateWithLifecycle().value,

                onSignUp = { viewModel.signUp() },
            )
        }
    )
}