package eu.project.aiesla.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.routeSignedIn.routeHomeScreen.subscreens.home.impl.homeImpl
import eu.project.aiesla.core.routeSignedIn.routeStudyScreen.subscreens.subject.subjectImpl
import eu.project.aiesla.core.routeSignedIn.routeStudyScreen.subscreens.subjects.subjectsImpl
import eu.project.aiesla.core.routeSignedOut.routeSignIn.passwordRecoveryEmailInformation.passwordRecoveryEmailInformationImpl
import eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.recoverYourPasswordImpl
import eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.impl.signInImpl
import eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp.signUpImpl
import eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation.signUpEmailInformationImpl
import eu.project.aiesla.core.routeSignedOut.welcomeScreenImpl
import eu.project.aiesla.core.screenDock.impl.screenDockImpl
import eu.project.aiesla.sharedConstants.navigation.Navigation

@Composable
fun core(
    authenticationManager: AuthenticationManager
) {

    val navHostController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {  },
        bottomBar = {

            screenDockImpl(navHostController = navHostController)
        },
        content = {

            NavHost(
                navController = navHostController,
                startDestination =
                    when (authenticationManager.isSignedIn()) {

                        false -> Navigation.SignedOut.RouteSignedOut
                        true -> Navigation.SignedIn.RouteSignedIn
                    },
                builder = {

                    navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

                        welcomeScreenImpl(navHostController = navHostController)

                        navigation<Navigation.SignedOut.SignIn.RouteSignIn>(startDestination = Navigation.SignedOut.SignIn.SignInScreen) {

                            signInImpl(
                                navHostController = navHostController,
                            )

                            recoverYourPasswordImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )

                            passwordRecoveryEmailInformationImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )
                        }

                        navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

                            signUpImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )

                            signUpEmailInformationImpl(navHostController = navHostController)
                        }
                    }

                    navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.Home.RouteHomeScreen) {

                        navigation<Navigation.SignedIn.Home.RouteHomeScreen>(startDestination = Navigation.SignedIn.Home.HomeSubscreen) {

                            homeImpl()
                        }

                        navigation<Navigation.SignedIn.Study.RouteStudyScreen>(startDestination = Navigation.SignedIn.Study.SubjectsSubscreen) {

                            subjectsImpl(navHostController = navHostController)

                            subjectImpl(navHostController = navHostController)
                        }
                    }
                }
            )
        }
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}