package eu.project.aiesla.sharedConstants.navigation

import androidx.navigation.NavDestination

enum class CurrentMainRoute {
    SignedIn,
    SignedOut,

    // in case something goes wrong
    RouteIsUnknown,
    RouteIsNull
}

fun NavDestination.getCurrentMainRoute(): CurrentMainRoute {

    val route = this.route ?: return CurrentMainRoute.RouteIsNull

    return when {
        route.contains(Navigation.SignedOut::class.java.simpleName) -> CurrentMainRoute.SignedOut
        route.contains(Navigation.SignedIn::class.java.simpleName) -> CurrentMainRoute.SignedIn
        else -> CurrentMainRoute.RouteIsUnknown
    }
}