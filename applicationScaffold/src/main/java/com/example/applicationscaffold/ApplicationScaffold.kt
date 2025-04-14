package com.example.applicationscaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.authentication.results.AuthenticationState
import com.example.routesignedin.routeSignedInImpl
import com.example.routesignedout.routeSignedOutImpl
import com.example.routeunsuccessfulinitialization.routeUnsuccessfulInitializationImpl

@Composable
fun applicationScaffold(authenticationState: AuthenticationState) {

    val navigationController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {},
        bottomBar = {},

        content = {

            NavHost(
                navController = navigationController,
                startDestination = when(authenticationState) {

                    AuthenticationState.SignedIn -> Navigation.SignedIn.RouteSignedIn
                    AuthenticationState.SignedOut -> Navigation.SignedOut.RouteSignedOut
                    is AuthenticationState.Unsuccessful -> Navigation.UnsuccessfulAuthentication.RouteUnsuccessfulAuthentication
                },
                builder = {

                    this.routeSignedOutImpl(navigationController = navigationController)

                    this.routeSignedInImpl()

                    this.routeUnsuccessfulInitializationImpl()
                }
            )
        }
    )
}