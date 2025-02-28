package eu.project.aiesla.core.routeSignedOut.signUp.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.routeSignedOut.signUp.ui.signUpScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.signUpImpl(
    navHostController: NavHostController,
    authenticationManager: AuthenticationManager
) {

    composable<Navigation.SignedOut.SignUp.SignUpScreen> {

        signUpScreen(

            onSignUp = {

//                authenticationManager.signUp(
//                    credentials = it
//                )

                navHostController.navigate(
                    route = Navigation.SignedOut.SignUp.VerifyYourEmailScreen,
                    builder = {
                        this.popUpTo(
                            route = Navigation.SignedOut.WelcomeScreen,
                            popUpToBuilder = { inclusive = false }
                        )
                    }
                )
            },
        )
    }
}