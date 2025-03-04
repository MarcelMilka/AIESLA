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
    fun `getCurrentScreen properly detects HomeScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.xnx.sharedConstants.navigation.Navigation.SignedIn.Home.HomeScreen"
        assertEquals(CurrentScreen.HomeScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen properly detects StudyScreen route`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.xnx.sharedConstants.navigation.Navigation.SignedIn.Study.StudyScreen"
        assertEquals(CurrentScreen.StudyScreen, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen returns Error when route is unknown`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "invalid.route.notMatching"
        assertEquals(CurrentScreen.Error, navDestination.getCurrentScreen())
    }

    @Test
    fun `getCurrentScreen returns Error when route is null`() {
        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns null
        assertEquals(CurrentScreen.Error, navDestination.getCurrentScreen())
    }
}

