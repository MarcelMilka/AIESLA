package eu.project.aiesla.sharedConstants.navigation

import androidx.navigation.NavDestination

enum class CurrentScreen {

    // signed out
        WelcomeScreen,

        // sign in
        SignUpScreen,
        SignUpEmailInformationScreen,

        // sign up
        SignInScreen,
        RecoverYourPasswordScreen,
        PasswordRecoveryEmailInformationScreen,

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
            route.contains(Navigation.SignedOut.SignIn.PasswordRecoveryEmailInformationScreen::class.java.simpleName) -> CurrentScreen.PasswordRecoveryEmailInformationScreen

            // sign up
            route.contains(Navigation.SignedOut.SignUp.SignUpScreen::class.java.simpleName) -> CurrentScreen.SignUpScreen
            route.contains(Navigation.SignedOut.SignUp.SignUpEmailInformationScreen::class.java.simpleName) -> CurrentScreen.SignUpEmailInformationScreen

        // signed in

            // home
            route.contains(Navigation.SignedIn.Home.HomeSubscreen::class.java.simpleName) -> CurrentScreen.HomeScreen

            // study
            route.contains(Navigation.SignedIn.Study.SubjectsSubscreen::class.java.simpleName) -> CurrentScreen.StudyScreen
            route.contains(Navigation.SignedIn.Study.SubjectSubscreen::class.java.simpleName) -> CurrentScreen.StudyScreen


        else -> CurrentScreen.Error
    }
}