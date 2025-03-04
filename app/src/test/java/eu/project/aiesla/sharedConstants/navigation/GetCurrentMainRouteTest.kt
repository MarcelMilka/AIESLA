package eu.project.aiesla.sharedConstants.navigation

import androidx.navigation.NavDestination
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class GetCurrentMainRouteTest {

    @Test
    fun `getCurrentMainRoute properly detects SignedOut route`() {

        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.aiesla.sharedConstants.navigation.Navigation.SignedOut.WelcomeScreen"

        assertEquals(
            CurrentMainRoute.SignedOut,
            navDestination.getCurrentMainRoute()
        )
    }

    @Test
    fun `getCurrentMainRoute properly detects SignedIn route`() {

        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "eu.project.xnx.sharedConstants.navigation.Navigation.SignedIn.HomeScreen"

        assertEquals(
            CurrentMainRoute.SignedIn,
            navDestination.getCurrentMainRoute()
        )
    }

    @Test
    fun `getCurrentMainRoute returns Error when route is unknown`() {

        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns "invalid.route.notMatching"

        assertEquals(
            CurrentMainRoute.Error,
            navDestination.getCurrentMainRoute()
        )
    }

    @Test
    fun `getCurrentMainRoute returns Error when route is null`() {

        val navDestination: NavDestination = mockk()
        every { navDestination.route } returns null

        assertEquals(
            CurrentMainRoute.Error,
            navDestination.getCurrentMainRoute()
        )
    }
}