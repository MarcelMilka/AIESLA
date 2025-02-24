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
import eu.project.aiesla.sharedConstants.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

//          findNavController()
            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {  },
                bottomBar = {  },
                content = {

                    NavHost(navController = navController, startDestination = Navigation.SignedOut.RouteSignedOut) {

                        navigation<Navigation.SignedOut.RouteSignedOut>(startDestination = Navigation.SignedOut.WelcomeScreen) {

                            composable<Navigation.SignedOut.WelcomeScreen> {

                                Text("welcome screen")
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