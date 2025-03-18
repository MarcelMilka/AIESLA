package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.impl

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.ui.signInScreen
import eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.vm.SignInScreenViewModel
import eu.project.aiesla.sharedConstants.navigation.Navigation
import kotlinx.coroutines.delay

fun NavGraphBuilder.signInImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignIn.SignInScreen> {

        val signInScreenViewModel = hiltViewModel<SignInScreenViewModel>()

        val signInProcess by
            signInScreenViewModel
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
            credentials = signInScreenViewModel.credentials.collectAsStateWithLifecycle().value,

            onUpdateEmail = {

                signInScreenViewModel.updateEmail(it)
            },
            onUpdatePassword = {

                signInScreenViewModel.updatePassword(it)
            },

            emailHintViewState = signInScreenViewModel.stateOfEmailHint.collectAsStateWithLifecycle().value,
            passwordHintViewState = signInScreenViewModel.stateOfPasswordHint.collectAsStateWithLifecycle().value,
            buttonProceedViewState = signInScreenViewModel.stateOfButtonProceed.collectAsState().value,

            onSignIn = {

                signInScreenViewModel.signIn()
            },
            onRecoverPassword = {

                navHostController.navigate(
                    route = Navigation.SignedOut.SignIn.RecoverYourPasswordScreen
                )
            }
        )
    }
}