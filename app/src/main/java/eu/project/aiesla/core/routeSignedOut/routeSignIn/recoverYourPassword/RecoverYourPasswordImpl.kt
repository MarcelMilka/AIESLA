package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword

import android.util.Log
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

        LaunchedEffect(passwordRecoveryProcess) {

            when (passwordRecoveryProcess) {
                PasswordRecoveryProcess.Idle -> {Log.d("Halla!", "Idle")}
                PasswordRecoveryProcess.Pending -> {Log.d("Halla!", "Pending")}
                PasswordRecoveryProcess.Successful -> {

                    Log.d("Halla!", "Successful")

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
                is PasswordRecoveryProcess.Unsuccessful -> {Log.d("Halla!", "Unsuccessful")}
            }
        }

        recoverYourPasswordScreen(
            onRecoverPassword = {

                authenticationManager.sendPasswordRecoveryEmail(email = it)
            }
        )
    }
}