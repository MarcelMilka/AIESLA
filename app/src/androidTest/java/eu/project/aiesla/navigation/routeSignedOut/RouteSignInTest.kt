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
class RouteSignInTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val acr = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var authenticationManager: AuthenticationManager



    // WelcomeScreen
    private val welcomeScreen = acr.onNodeWithTag("WelcomeScreen")
    private val buttonSignIn = acr.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign in.'")

    // SignInScreen
    private val signInScreen = acr.onNodeWithTag("SignInScreen")
    private val signInScreenEmailTextField = acr.onNodeWithTag("SignInScreen emailTextField")
    private val signInScreenPasswordTextField = acr.onNodeWithTag("SignInScreen passwordTextField")
    private val signInScreenButtonSignIn = acr.onNodeWithTag("SignInScreen button 'Sign in'")
    private val buttonRecoverYourPassword = acr.onNodeWithTag("SignInScreen button 'Recover your password")

    // RecoverYourPasswordScreen
    private val recoverYourPasswordScreen = acr.onNodeWithTag("RecoverYourPasswordScreen")
    private val recoverYourPasswordScreenEmailTextField = acr.onNodeWithTag("RecoverYourPasswordScreen emailTextField")
    private val recoverYourPasswordScreenButtonRecoverPassword = acr.onNodeWithTag("RecoverYourPasswordScreen `recover your password`")

    // PasswordRecoveryEmailInformationScreen
    private val passwordRecoveryEmailInformationScreen = acr.onNodeWithTag("PasswordRecoveryEmailInformationScreen")
    private val passwordRecoveryEmailInformationButtonSignIn = acr.onNodeWithTag("PasswordRecoveryEmailInformationScreen primaryAuthenticationTextButton 'Sign in.'")

    // HomeSubscreen
    private val homeSubscreen = acr.onNodeWithTag("HomeSubscreen")



    @Before
    fun before() {

        hiltRule.inject()

        welcomeScreen.assertIsDisplayed()
    }



    @Test
    fun `welcomeScreen navigates to signInScreen - signInScreen back to welcomeScreen`() = runTest {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // verify sign signInScreen
        signInScreen.assertIsDisplayed()

        // navigate back to welcome screen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify welcomeScreen
        welcomeScreen.assertIsDisplayed()
    }

    @Test
    fun `signInScreen navigates to homeSubscreen - homeSubscreen prevents back navigation`() = runTest {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // navigate to homeSubscreen
        signInScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        signInScreenPasswordTextField.performTextInput("StrongPassword123@@@")
        signInScreenButtonSignIn.performClick()

        // wait at least 100 ms to navigate to homeSubscreen
        acr.waitUntilTimeout(110L)

        // verify homeSubscreen
        homeSubscreen.assertIsDisplayed()

        // homeSubscreen prevents back navigation
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    @Test
    fun `signInScreen navigates to recoverYourPasswordScreen - recoverYourPasswordScreen back to signInScreen`() = runTest {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // navigate to recoverYourPasswordScreen
        buttonRecoverYourPassword.performClick()

        // verify sign recoverYourPasswordScreen
        recoverYourPasswordScreen.assertIsDisplayed()

        // navigate back to signInScreen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify sign signInScreen
        signInScreen.assertIsDisplayed()
    }

    @Test
    fun `recoverYourPasswordScreen navigates to passwordRecoveryEmailInformationScreen - passwordRecoveryEmailInformationScreen back to welcomeScreen`() = runTest {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // navigate to recoverYourPasswordScreen
        buttonRecoverYourPassword.performClick()

        // navigate to passwordRecoveryEmailInformationScreen
        recoverYourPasswordScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        recoverYourPasswordScreenButtonRecoverPassword.performClick()

        // wait at least 100 ms to navigate to passwordRecoveryEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify passwordRecoveryEmailInformationScreen
        passwordRecoveryEmailInformationScreen.assertIsDisplayed()

        // navigate back to welcomeScreen
        acr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        // verify welcomeScreen
        welcomeScreen.assertIsDisplayed()
    }

    @Test
    fun `recoverYourPasswordScreen navigates to passwordRecoveryEmailInformationScreen - passwordRecoveryEmailInformationScreen navigates to signInScreen`() {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // navigate to recoverYourPasswordScreen
        buttonRecoverYourPassword.performClick()

        // navigate to passwordRecoveryEmailInformationScreen
        recoverYourPasswordScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        recoverYourPasswordScreenButtonRecoverPassword.performClick()

        // wait at least 100 ms to navigate to passwordRecoveryEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify passwordRecoveryEmailInformationScreen
        passwordRecoveryEmailInformationScreen.assertIsDisplayed()

        // navigate to signInScreen
        passwordRecoveryEmailInformationButtonSignIn.performClick()

        // verify signInScreen
        signInScreen.assertIsDisplayed()
    }

    @Test
    fun `passwordRecoveryEmailInformationScreen navigates to signInScreen - signInScreen back to welcomeScreen`() {

        // navigate to signInScreen
        buttonSignIn.performClick()

        // navigate to recoverYourPasswordScreen
        buttonRecoverYourPassword.performClick()

        // navigate to passwordRecoveryEmailInformationScreen
        recoverYourPasswordScreenEmailTextField.performTextInput("strongEmail.google@gmail.com")
        recoverYourPasswordScreenButtonRecoverPassword.performClick()

        // wait at least 100 ms to navigate to passwordRecoveryEmailInformationScreen
        acr.waitUntilTimeout(110L)

        // verify passwordRecoveryEmailInformationScreen
        passwordRecoveryEmailInformationScreen.assertIsDisplayed()

        // navigate to signInScreen
        passwordRecoveryEmailInformationButtonSignIn.performClick()

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