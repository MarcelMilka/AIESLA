package com.example.applicationscaffold

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.authentication.results.InitialAuthenticationState
import com.example.authentication.results.UnsuccessfulInitializationCause
import org.junit.Rule
import org.junit.Test

class ApplicationScaffoldTest {

    @get:Rule
    val ctr = createComposeRule()

    @Test
    fun `WelcomeScreen is displayed when authentication state is SignedOut`() {

        // preparation
        ctr.setContent {

            applicationScaffold(InitialAuthenticationState.SignedOut)
        }

        // test
        ctr.onNodeWithTag("WelcomeScreen").assertIsDisplayed()
    }

    @Test
    fun `HomeScreen is displayed when authentication state is SignedIn`() {

        // preparation
        ctr.setContent {

            applicationScaffold(InitialAuthenticationState.SignedIn)
        }

        // test
        ctr.onNodeWithTag("HomeScreen").assertIsDisplayed()
    }

    @Test
    fun `UnsuccessfulInitializationScreen is displayed when authentication state is UnsuccessfulInitialization`() {

        // preparation
        ctr.setContent {

            applicationScaffold(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null))
        }

        // test
        ctr.onNodeWithTag("UnsuccessfulInitializationScreen").assertIsDisplayed()
    }
}