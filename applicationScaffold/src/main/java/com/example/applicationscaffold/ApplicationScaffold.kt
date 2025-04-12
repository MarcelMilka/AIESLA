package com.example.applicationscaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.routesignedout.routeSignedOutImpl

@Composable
fun applicationScaffold() {

    val navigationController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {},
        bottomBar = {},

        content = {

            NavHost(
                navController = navigationController,
                startDestination = Navigation.SignedOut.RouteSignedOut,
                builder = {

                    this.routeSignedOutImpl()
                }
            )
        }
    )
}