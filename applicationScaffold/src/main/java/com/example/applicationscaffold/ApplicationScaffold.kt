package com.example.applicationscaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.authentication.results.InitialAuthenticationState
import com.example.routesignedin.routeSignedInImpl
import com.example.routesignedout.routeSignedOutImpl
import com.example.routeunsuccessfulinitialization.routeUnsuccessfulInitializationImpl

@Composable
fun applicationScaffold(authenticationState: InitialAuthenticationState) {

    val navigationController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {},
        bottomBar = {},

        content = {

            NavHost(
                navController = navigationController,
                startDestination = when(authenticationState) {

                    InitialAuthenticationState.SignedIn -> Navigation.SignedIn.RouteSignedIn
                    InitialAuthenticationState.SignedOut -> Navigation.SignedOut.RouteSignedOut
                    is InitialAuthenticationState.Unsuccessful -> Navigation.UnsuccessfulAuthentication.RouteUnsuccessfulAuthentication
                },
                builder = {

                    this.routeSignedOutImpl(navigationController = navigationController)

                    this.routeSignedInImpl(navigationController = navigationController)

                    this.routeUnsuccessfulInitializationImpl()
                }
            )
        }
    )
}