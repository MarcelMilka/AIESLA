package com.example.routesignedout.welcomeScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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

    private val buttonSignIn = composeTestRule.onNodeWithTag("WelcomeScreen - button 'Sign in.'")
    private val buttonSignUp = composeTestRule.onNodeWithTag("WelcomeScreen - button 'Sign up.'")

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
    fun `onSignIn is called after clicking the button sign in`() = runTest {

        // make sure everything's displayed
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()

        // test the button 'Sign in.'
        buttonSignIn.performClick()
        composeTestRule.awaitIdle()
        assertTrue(signInClicked)
    }

    @Test
    fun `onSignUp is called after clicking the button sign up`() = runTest {

        // make sure everything's displayed
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()

        // test the button 'Sign up.'
        buttonSignUp.performClick()
        composeTestRule.awaitIdle()
        assertTrue(signUpClicked)
    }
}