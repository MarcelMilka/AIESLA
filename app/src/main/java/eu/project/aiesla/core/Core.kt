package eu.project.aiesla.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.routeSignedIn.home.impl.homeImpl
import eu.project.aiesla.core.routeSignedIn.study.impl.studyImpl
import eu.project.aiesla.core.routeSignedOut.checkYourEmail.impl.checkYourEmailImpl
import eu.project.aiesla.core.routeSignedOut.recoverYourPassword.impl.recoverYourPasswordImpl
import eu.project.aiesla.core.routeSignedOut.signIn.impl.signInImpl
import eu.project.aiesla.core.routeSignedOut.signUp.impl.signUpImpl
import eu.project.aiesla.core.routeSignedOut.verifyYourEmail.impl.verifyYourEmailImpl
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

                            checkYourEmailImpl()
                        }

                        navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

                            signUpImpl(
                                navHostController = navHostController,
                                authenticationManager = authenticationManager
                            )

                            verifyYourEmailImpl()
                        }
                    }

                    navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.Home.RouteHome) {

                        navigation<Navigation.SignedIn.Home.RouteHome>(startDestination = Navigation.SignedIn.Home.HomeScreen) {

                            homeImpl()
                        }

                        navigation<Navigation.SignedIn.Study.RouteStudy>(startDestination = Navigation.SignedIn.Study.StudyScreen) {

                            studyImpl()
                        }
                    }
                }
            )
        }
    )
}