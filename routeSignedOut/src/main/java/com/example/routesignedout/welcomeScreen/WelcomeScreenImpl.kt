package com.example.routesignedout.welcomeScreen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation

fun NavGraphBuilder.welcomeScreenImpl(navigationController: NavHostController) {

    composable<Navigation.SignedOut.WelcomeScreen>(

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            welcomeScreen(
                onSignIn = {

//                    navigationController.navigate(
//                        route = Navigation.SignedOut.SignIn.RouteSignIn
//                    )
                },

                onSignUp = {

//                    navigationController.navigate(
//                        route = Navigation.SignedOut.SignUp.RouteSignUp
//                    )
                }
            )
        }
    )
}