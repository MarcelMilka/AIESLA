package com.example.routesignedin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.example.navigation.Navigation
import com.example.routesignedin.routeHome.homeScreenImpl
import com.example.routesignedin.routeStudy.subjects.impl.subjectsScreenImpl

fun NavGraphBuilder.routeSignedInImpl(navigationController: NavHostController) {

    navigation<Navigation.SignedIn.RouteSignedIn>(startDestination = Navigation.SignedIn.Study.RouteStudy) {

        this.navigation<Navigation.SignedIn.Home.RouteHome>(startDestination = Navigation.SignedIn.Home.HomeScreen) {

            this.homeScreenImpl(navigationController = navigationController)
        }

        this.navigation<Navigation.SignedIn.Study.RouteStudy>(startDestination = Navigation.SignedIn.Study.SubjectsScreen) {

            this.subjectsScreenImpl(navigationController = navigationController)
        }
    }
}