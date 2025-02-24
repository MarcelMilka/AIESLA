package eu.project.aiesla.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui.welcomeScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {  },
                bottomBar = {  },
                content = {

                    NavHost(navController = navController, startDestination = Navigation.SignedOut.RouteSignedOut) {

                        navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

                            composable<Navigation.SignedOut.WelcomeScreen> {

                                welcomeScreen(
                                    onSignIn = {  },
                                    onSignUp = {

                                        navController.navigate(route = Navigation.SignedOut.SignUp.RouteSignUp)
                                    },
                                    onContinueWithoutAccount = {

                                        navController.navigate(route = Navigation.SignedIn.RouteSignedIn)
                                    }
                                )
                            }

                            navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

                                composable<Navigation.SignedOut.SignUp.SignUpScreen> {

                                    Text("sign up screen")
                                }

                                composable<Navigation.SignedOut.SignUp.SignUpWitEmailAndPasswordScreen> {

                                    Text("sign up with email and password screen")
                                }

                                composable<Navigation.SignedOut.SignUp.ConfirmYourRegistrationScreen> {

                                    Text("confirm your registration screen")
                                }
                            }
                        }

                        navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.PodcastsScreen) {

                            composable<Navigation.SignedIn.PodcastsScreen> {

                                Text("welcome screen")
                            }
                        }
                    }
                }
            )
        }
    }
}