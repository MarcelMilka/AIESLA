package eu.project.aiesla.sharedConstants.navigation

import androidx.navigation.NavDestination

enum class CurrentScreen {

    // signed out
        WelcomeScreen,

        // sign in
        SignUpScreen,
        VerifyYourEmailScreen,

        // sign up
        SignInScreen,
        RecoverYourPasswordScreen,
        CheckYourEmailScreen,

    // signed in
    PodcastsScreen,

    // in case something goes wrong
    RouteIsUnknown,
    RouteIsNull
}

fun NavDestination.getCurrentScreen(): CurrentScreen {

    val route = this.route ?: return CurrentScreen.RouteIsNull

    return when {

        // signed out
            route.contains(Navigation.SignedOut.WelcomeScreen::class.java.simpleName) -> CurrentScreen.WelcomeScreen

            // sign in
            route.contains(Navigation.SignedOut.SignIn.SignInScreen::class.java.simpleName) -> CurrentScreen.SignInScreen
            route.contains(Navigation.SignedOut.SignIn.RecoverYourPasswordScreen::class.java.simpleName) -> CurrentScreen.RecoverYourPasswordScreen
            route.contains(Navigation.SignedOut.SignIn.CheckYourEmailScreen::class.java.simpleName) -> CurrentScreen.CheckYourEmailScreen

            // sign up
            route.contains(Navigation.SignedOut.SignUp.SignUpScreen::class.java.simpleName) -> CurrentScreen.SignUpScreen
            route.contains(Navigation.SignedOut.SignUp.VerifyYourEmailScreen::class.java.simpleName) -> CurrentScreen.VerifyYourEmailScreen

        // signed in
        route.contains(Navigation.SignedIn.PodcastsScreen::class.java.simpleName) -> CurrentScreen.PodcastsScreen

        else -> CurrentScreen.RouteIsUnknown
    }
}