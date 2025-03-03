package eu.project.aiesla.sharedConstants.navigation

import androidx.navigation.NavDestination
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class GetCurrentScreenTest {

    @Test
    fun `getCurrentScreen properly detects WelcomeScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.WelcomeScreen"
        assertEquals(CurrentScreen.WelcomeScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects SignInScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.SignIn.SignInScreen"
        assertEquals(CurrentScreen.SignInScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects RecoverYourPasswordScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.SignIn.RecoverYourPasswordScreen"
        assertEquals(CurrentScreen.RecoverYourPasswordScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects CheckYourEmailScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.SignIn.CheckYourEmailScreen"
        assertEquals(CurrentScreen.CheckYourEmailScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects SignUpScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.SignUp.SignUpScreen"
        assertEquals(CurrentScreen.SignUpScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects VerifyYourEmailScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.SignUp.VerifyYourEmailScreen"
        assertEquals(CurrentScreen.VerifyYourEmailScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects PodcastsScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.xnx.sharedConstants.navigation.Navigation.SignedIn.PodcastsScreen"
        assertEquals(CurrentScreen.PodcastsScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen returns RouteIsUnknown when route is unknown`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "invalid.route.notMatching"
        assertEquals(CurrentScreen.RouteIsUnknown, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen returns RouteIsNull when route is null`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns null
        assertEquals(CurrentScreen.RouteIsNull, navDestination.getCurrentScreen())
    }
}

