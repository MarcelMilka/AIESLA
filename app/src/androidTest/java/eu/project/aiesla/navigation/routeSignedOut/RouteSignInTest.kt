package eu.project.aiesla.navigation.routeSignedOut

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.core.MainActivity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RouteSignInTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val ctr = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Before fun before() {

        hiltRule.inject()
    }

    @Test
    fun `WelcomeScreen to SignInScreen and navigate back`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignInScreen'
        buttonSignIn.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()

        // navigate back to 'WelcomeScreen'
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()
    }

    @Test
    fun `WelcomeScreen to SignInScreen to PodcastsScreen and navigate back`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignInScreen'
        buttonSignIn.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()

        // navigate to 'PodcastsScreen'
        emailTextField.performTextInput("google.user@gmail.com")
        ctr.awaitIdle()
        passwordTextField.performTextInput("1@Aaaaaaaaaaaaaaaa")
        ctr.awaitIdle()

        buttonSignIn.assertIsDisplayed()
        buttonSignIn.performClick()
        ctr.awaitIdle()

        ctr.onNodeWithText("Home subscreen").assertIsDisplayed()

        // navigate back (leave the app)
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    @Test
    fun `WelcomeScreen to SignInScreen to RecoverYourPasswordScreen and navigate back twice`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignInScreen'
        buttonSignIn.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()

        // navigate to 'RecoverYourPasswordScreen'
        buttonRecoverYourPassword.performClick()

        emailTextField.assertIsDisplayed()
        buttonRecoverYourPassword.assertIsNotDisplayed()

        // navigate back (SignInScreen)
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()

        // navigate back (WelcomeScreen)
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()
    }

    @Test
    fun `WelcomeScreen to SignInScreen to RecoverYourPasswordScreen to CheckYourEmailScreen and navigate back twice`() = runTest {

        // 'WelcomeScreen' is set by default
        buttonSignIn.assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonContinueWithoutAccount.assertIsDisplayed()

        // navigate to 'SignInScreen'
        buttonSignIn.performClick()

        emailTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        buttonSignUp.assertIsNotDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()

        // navigate to 'RecoverYourPasswordScreen'
        buttonRecoverYourPassword.performClick()

        emailTextField.assertIsDisplayed()
        buttonRecoverYourPassword.assertIsNotDisplayed()

        // navigate to 'CheckYourEmailScreen'
        emailTextField.performTextInput("aaaaaaaaaaaaaaaaaaaaa")
        ctr.awaitIdle()

        buttonRecoverYourPassword.assertIsDisplayed()
        buttonRecoverYourPassword.performClick()

        bigPrimaryLabel.assertIsDisplayed()

        // navigate back (WelcomeScreen)
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

    // SignInScreen
    private val emailTextField = ctr.onNodeWithTag("emailTextField")
    private val passwordTextField = ctr.onNodeWithTag("passwordTextField")
    private val buttonRecoverYourPassword = ctr.onNodeWithTag("button 'Recover your password.'")

    // VerifyYourEmailScreen
    private val bigPrimaryLabel = ctr.onNodeWithTag("bigPrimaryLabel")
}