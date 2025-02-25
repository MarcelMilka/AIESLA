package eu.project.aiesla.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.project.aiesla.auth.AuthenticationManager
import eu.project.aiesla.main.routeSignedOut.signInScreen.impl.signInScreenImpl
import eu.project.aiesla.main.routeSignedOut.signUpScreen.impl.signUpScreenImpl
import eu.project.aiesla.main.routeSignedOut.welcomeScreen.impl.welcomeScreenImpl
import eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui.welcomeScreen
import eu.project.aiesla.sharedConstants.navigation.Navigation
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authenticationManager: AuthenticationManager

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

                    NavHost(
                        navController = navController,
                        startDestination =
                            when (authenticationManager.isSignedIn()) {

                                false -> Navigation.SignedOut.RouteSignedOut
                                true -> Navigation.SignedIn.RouteSignedIn
                            },
                        builder = {

                            navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

                                welcomeScreenImpl(
                                    navHostController = navController,
                                )

                                navigation<Navigation.SignedOut.SignUp.RouteSignUp>(startDestination = Navigation.SignedOut.SignUp.SignUpScreen) {

                                    signUpScreenImpl(
                                        navHostController = navController,
                                        onSignUp = { credentials ->

                                            Log.d("Halla!", "sign up")
                                            authenticationManager.signUp(credentials = credentials)
                                        }
                                    )

                                    composable<Navigation.SignedOut.SignUp.SignUpWitEmailAndPasswordScreen> {

                                        Text("sign up with email and password screen")
                                    }

                                    composable<Navigation.SignedOut.SignUp.ConfirmYourRegistrationScreen> {

                                        Text("confirm your registration screen")
                                    }
                                }

                                navigation<Navigation.SignedOut.SignIn.RouteSignIn>(startDestination = Navigation.SignedOut.SignIn.SignInScreen) {

                                    signInScreenImpl(
                                        navHostController = navController,
                                        onSignIn = { authenticationManager.signIn(it) }
                                    )
                                }
                            }

                            navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.PodcastsScreen) {

                                composable<Navigation.SignedIn.PodcastsScreen> {

                                    Text("podcasts screen")

                                    Button(onClick = {authenticationManager.signOut()}, content = {})
                                }
                            }
                        }
                    )
                }
            )
        }
    }
}