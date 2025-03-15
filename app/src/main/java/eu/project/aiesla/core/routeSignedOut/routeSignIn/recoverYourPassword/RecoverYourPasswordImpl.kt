package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.recoverYourPasswordImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignIn.RecoverYourPasswordScreen> {

        val passwordRecoveryProcess by
            authenticationManager
            .passwordRecoveryProcess
            .collectAsStateWithLifecycle()

        LaunchedEffect(true) {

            authenticationManager.resetStatesOfProcesses()
        }

        LaunchedEffect(passwordRecoveryProcess) {

            if (passwordRecoveryProcess is PasswordRecoveryProcess.Successful) {

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

            passwordRecoveryProcess = passwordRecoveryProcess,

            onResetPasswordRecoveryProcess = {

                authenticationManager.resetStatesOfProcesses()
            },

            onRecoverPassword = {

                authenticationManager.sendPasswordRecoveryEmail(email = it)
            }
        )
    }
}