package eu.project.aiesla.core.routeSignedOut

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import eu.project.aiesla.core.routeSignedOut.routeSignUp.welcome.ui.welcomeScreen
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var signInClicked = false
    private var signUpClicked = false

    private val signInButton = composeTestRule.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign in.'")
    private val signUpButton = composeTestRule.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign up.'")

    @Before
    fun before() {

        signInClicked = false
        signUpClicked = false

        composeTestRule.setContent {
            welcomeScreen(
                onSignIn = { signInClicked = true },
                onSignUp = { signUpClicked = true }
            )
        }
    }

    @Test
    fun b() = runTest {

        // make sure everything's displayed
        signInButton.assertIsDisplayed()
        signUpButton.assertIsDisplayed()

        // test the button 'sign in'
        signInButton.performClick()
        composeTestRule.awaitIdle()
        assertTrue(signInClicked)
    }

    @Test
    fun a() = runTest {

        // make sure everything's displayed
        signInButton.assertIsDisplayed()
        signUpButton.assertIsDisplayed()

        // test the button 'sign in'
        signUpButton.performClick()
        composeTestRule.awaitIdle()
        assertTrue(signUpClicked)
    }
}