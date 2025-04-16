package com.example.routesignedout.routeSignUp.signUpEmailInformation.impl

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation.Navigation
import com.example.routesignedout.routeSignUp.signUpEmailInformation.ui.signUpEmailInformationScreen

fun NavGraphBuilder.signUpEmailInformationScreenImpl(navHostController: NavHostController) {

    composable<Navigation.SignedOut.SignUp.SignUpEmailInformationScreen> (

        enterTransition = { EnterTransition.None },

        exitTransition = { ExitTransition.None },

        content = {

            signUpEmailInformationScreen(
                onSignIn = {

//                    navHostController.navigate(
//                        route = Navigation.SignedOut.SignIn.RouteSignIn,
//                        builder = {
//
//                            this.popUpTo(
//                                route = Navigation.SignedOut.WelcomeScreen,
//                                popUpToBuilder = { inclusive = false }
//                            )
//                        }
//                    )
                }
            )
        }
    )
}