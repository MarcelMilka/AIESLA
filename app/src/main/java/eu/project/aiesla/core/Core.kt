package eu.project.aiesla.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.routeSignedIn.homeScreen.subscreens.home.impl.homeImpl
import eu.project.aiesla.core.routeSignedIn.studyScreen.subscreens.subjects.impl.subjectsImpl
import eu.project.aiesla.core.routeSignedOut.checkYourEmail.impl.passwordRecoveryEmailInformationImpl
import eu.project.aiesla.core.routeSignedOut.recoverYourPassword.impl.recoverYourPasswordImpl
import eu.project.aiesla.core.routeSignedOut.signIn.impl.signInImpl
import eu.project.aiesla.core.routeSignedOut.signUp.impl.signUpImpl
import eu.project.aiesla.core.routeSignedOut.signUpEmailInformation.impl.signUpEmailInformationImpl
import eu.project.aiesla.core.routeSignedOut.welcomeScreen.impl.welcomeScreenImpl
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
                                authenticationManager = authenticationManager
                            )

                            recoverYourPasswordImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )

                            passwordRecoveryEmailInformationImpl()
                        }

                        navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

                            signUpImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )

                            signUpEmailInformationImpl()
                        }
                    }

                    navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.Home.RouteHomeScreen) {

                        navigation<Navigation.SignedIn.Home.RouteHomeScreen>(startDestination = Navigation.SignedIn.Home.HomeSubscreen) {

                            homeImpl()
                        }

                        navigation<Navigation.SignedIn.Study.RouteStudyScreen>(startDestination = Navigation.SignedIn.Study.SubjectsSubscreen) {

                            subjectsImpl()
                        }
                    }
                }
            )
        }
    )
}