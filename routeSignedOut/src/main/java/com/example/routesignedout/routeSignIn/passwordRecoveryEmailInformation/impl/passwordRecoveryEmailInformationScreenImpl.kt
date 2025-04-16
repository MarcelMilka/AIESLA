package com.example.routesignedout.routeSignIn.passwordRecoveryEmailInformation.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignIn.passwordRecoveryEmailInformation.ui.passwordRecoveryEmailInformationScreen

internal fun NavGraphBuilder.passwordRecoveryEmailInformationScreenImpl(
    navHostController: NavHostController,
) {

    composable<Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen> (

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

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