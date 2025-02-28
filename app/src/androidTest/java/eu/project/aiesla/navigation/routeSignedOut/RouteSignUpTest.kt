package eu.project.aiesla.navigation.routeSignedOut

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import eu.project.aiesla.core.MainActivity
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RouteSignUpTest {

    @get:Rule val ctr = createAndroidComposeRule<MainActivity>()

    @Test fun `WelcomeScreen to SignUpScreen and navigate back`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignUpScreen'
        buttonSignUp.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()

        // navigate back to 'WelcomeScreen'
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()
    }

    @Test fun `WelcomeScreen to SignUpScreen to VerifyYourEmailScreen and navigate back`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignUpScreen'
        buttonSignUp.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()

        // navigate to 'VerifyYourEmailScreen'
        emailTextField.performTextInput("google.user@gmail.com")
        ctr.awaitIdle()
        passwordTextField.performTextInput("1@Aaaaaaaaaaaaaaaa")
        ctr.awaitIdle()

        buttonSignUp.assertIsDisplayed()
        buttonSignUp.performClick()

        bigPrimaryLabel.assertIsDisplayed()

        // navigate back to 'WelcomeScreen'
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()
    }

    // WelcomeScreen
    private val buttonSignIn = ctr.onNodeWithTag("button 'Sign in.'")
    private val buttonSignUp = ctr.onNodeWithTag("button 'Sign up.'")
    private val buttonContinueWithoutAccount = ctr.onNodeWithTag("button 'Continue without account.'")

    // SignUpScreen
    private val emailTextField = ctr.onNodeWithTag("emailTextField")
    private val passwordTextField = ctr.onNodeWithTag("passwordTextField")

    // VerifyYourEmailScreen
    private val bigPrimaryLabel = ctr.onNodeWithTag("bigPrimaryLabel")
}