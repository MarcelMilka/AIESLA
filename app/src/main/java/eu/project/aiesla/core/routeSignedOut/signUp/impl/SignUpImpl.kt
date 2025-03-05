package eu.project.aiesla.core.routeSignedOut.signUp.impl

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.SignUpProcess
import eu.project.aiesla.core.routeSignedOut.signUp.ui.signUpScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignUp.SignUpScreen> {

        val signUpProcess by
        authenticationManager
            .signUpProcess
            .collectAsStateWithLifecycle()

        LaunchedEffect(signUpProcess) {

            when (signUpProcess) {
                SignUpProcess.Idle -> {
                    Log.d("Halla!", "Idle asdad ")}
                SignUpProcess.Pending -> {
                    Log.d("Halla!", "Pending asdas ")}
                SignUpProcess.Successful -> {

                    Log.d("Halla!", "Successful asda ")

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
                is SignUpProcess.Unsuccessful -> {
                    Log.d("Halla!", "Unsuccessful asdad ")
                }
            }
        }

        signUpScreen(

            onSignUp = {

                authenticationManager.signUp(
                    credentials = it
                )
            },
        )
    }
}