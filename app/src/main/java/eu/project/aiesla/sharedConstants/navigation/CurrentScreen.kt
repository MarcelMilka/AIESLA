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
    HomeScreen,

    StudyScreen,

    // in case something goes wrong
    Error
}

fun NavDestination.getCurrentScreen(): CurrentScreen {

    val route = this.route ?: return CurrentScreen.Error

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

            // home
            route.contains(Navigation.SignedIn.Home.HomeScreen::class.java.simpleName) -> CurrentScreen.HomeScreen

            // study
            route.contains(Navigation.SignedIn.Study.StudyScreen::class.java.simpleName) -> CurrentScreen.StudyScreen


        else -> CurrentScreen.Error
    }
}