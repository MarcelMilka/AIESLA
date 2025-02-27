package eu.project.aiesla.main.routeSignedOut.recoverYourPassword.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.aiesla.main.routeSignedOut.recoverYourPassword.ui.recoverYourPasswordScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

fun NavGraphBuilder.recoverYourPasswordImpl(
    navHostController: NavHostController,
) {

    composable<Navigation.SignedOut.SignIn.RecoverYourPasswordScreen> {

        recoverYourPasswordScreen(
            onRecoverPassword = {

                navHostController.navigate(
                    route = Navigation.SignedOut.SignIn.CheckYourEmailScreen,
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
}