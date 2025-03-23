package eu.project.aiesla.navigation.routeSignedOut

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.MainActivity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltAndroidTest
class RouteSignUpTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val acr = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var authenticationManager: AuthenticationManager



    // welcomeScreen
    private val welcomeScreen = acr.onNodeWithTag("WelcomeScreen")
    private val buttonSignUp = acr.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign up.'")

    // signUpScreen
    private val signUpScreen = acr.onNodeWithTag("SignUpScreen")
    private val signUpScreenEmailTextField = acr.onNodeWithTag("SignUpScreen emailTextField")
    private val signUpScreenPasswordTextField = acr.onNodeWithTag("SignUpScreen passwordTextField")
    private val signUpScreenButtonSignUp = acr.onNodeWithTag("SignUpScreen button 'Sign up'")

    // signUpEmailInformationScreen
    private val signUpEmailInformationScreen = acr.onNodeWithTag("SignUpEmailInformationScreen")
    private val signUpEmailInformationScreenButtonSignIn = acr.onNodeWithTag("SignUpEmailInformationScreen primaryAuthenticationButton 'Sign in.'")

    // SignInScreen
    private val signInScreen = acr.onNodeWithTag("SignInScreen")


    @Before
    fun before() {

        hiltRule.inject()
    }



    @Test
    fun `welcomeScreen navigates to signUpScreen - signUpScreen back to welcomeScreen`() = runTest {

        // navigate to signUpScreen
        buttonSignUp.performClick()

        // verify sign signUpScreen
        signUpScreen.assertIsDisplayed()

        // navigate back to welcome screen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify welcomeScreen
        welcomeScreen.assertIsDisplayed()
    }

    @Test
    fun `signUpScreen navigates to signUpEmailInformationScreen - signUpEmailInformationScreen back to welcomeScreen`() = runTest {

        // navigate to signUpScreen
        buttonSignUp.performClick()

        // navigate to signUpEmailInformationScreen
        signUpScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        signUpScreenPasswordTextField.performTextInput("StrongPassword123@@@")
        signUpScreenButtonSignUp.performClick()

        // wait at least 100 ms to navigate to signUpEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify signUpEmailInformationScreen
        signUpEmailInformationScreen.assertIsDisplayed()

        // navigate back to welcomeScreen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify welcomeScreen
        welcomeScreen.assertIsDisplayed()
    }

    @Test
    fun `ignUpScreen navigates to signUpEmailInformationScreen - signUpEmailInformationScreen navigates to signInScreen`() {

        // navigate to signUpScreen
        buttonSignUp.performClick()

        // navigate to signUpEmailInformationScreen
        signUpScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        signUpScreenPasswordTextField.performTextInput("StrongPassword123@@@")
        signUpScreenButtonSignUp.performClick()

        // wait at least 100 ms to navigate to signUpEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify signUpEmailInformationScreen
        signUpEmailInformationScreen.assertIsDisplayed()

        // navigate to signInScreen
        signUpEmailInformationScreenButtonSignIn.performClick()

        // verify signInScreen
        signInScreen.assertIsDisplayed()
    }

    @Test
    fun `signUpEmailInformationScreen navigates to signInScreen - signInScreen back to welcomeScreen`() {

        // navigate to signUpScreen
        buttonSignUp.performClick()

        // navigate to signUpEmailInformationScreen
        signUpScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        signUpScreenPasswordTextField.performTextInput("StrongPassword123@@@")
        signUpScreenButtonSignUp.performClick()

        // wait at least 100 ms to navigate to signUpEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify signUpEmailInformationScreen
        signUpEmailInformationScreen.assertIsDisplayed()

        // navigate to signInScreen
        signUpEmailInformationScreenButtonSignIn.performClick()

        // verify signInScreen
        signInScreen.assertIsDisplayed()

        // navigate back to welcomeScreen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify welcomeScreen
        welcomeScreen.assertIsDisplayed()
    }

    private fun ComposeContentTestRule.waitUntilTimeout(timeoutMillis: Long) {

        AsyncTimer.start(timeoutMillis)

        this.waitUntil(
            condition = { AsyncTimer.expired },
            timeoutMillis = timeoutMillis + 1000
        )
    }

    object AsyncTimer {

        var expired = false

        fun start(delay: Long = 1000) {
            expired = false

            Timer().schedule(delay) {
                expired = true
            }
        }
    }
}