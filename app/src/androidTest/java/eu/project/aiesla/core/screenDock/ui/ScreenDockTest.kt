package eu.project.aiesla.core.screenDock.ui

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
class ScreenDockTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val ctr = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Before
    fun before() {

        hiltRule.inject()
    }

    @Test
    fun `content of screenDock is not displayed throughout whole SignIn route`() = runTest {

        // 1:verify content of screenDock is not displayed in 'WelcomeScreen'
        buttonHome.assertIsNotDisplayed()
        buttonStudy.assertIsNotDisplayed()
        ctr.awaitIdle()

        // 2: verify content of screenDock is not displayed in 'SignInScreen'

            // navigate to 'SignInScreen'
            buttonSignIn.performClick()
            ctr.awaitIdle()

            // verification
            buttonHome.assertIsNotDisplayed()
            buttonStudy.assertIsNotDisplayed()


        // 3: verify content of screenDock is not displayed in 'RecoverYourPasswordScreen'

            // navigate to 'RecoverYourPasswordScreen'
            buttonRecoverYourPassword.performClick()
            ctr.awaitIdle()

            // verification
            buttonHome.assertIsNotDisplayed()
            buttonStudy.assertIsNotDisplayed()


        // 4: verify content of screenDock is not displayed in 'PasswordRecoveryEmailInformationScreen'

            // navigate to 'PasswordRecoveryEmailInformationScreen'
            emailTextField.performTextInput("aaaaaaaaaaaaaaaa")
            ctr.awaitIdle()
            buttonRecoverYourPassword.performClick()
            ctr.awaitIdle()

            // verification
            buttonHome.assertIsNotDisplayed()
            buttonStudy.assertIsNotDisplayed()
    }

    @Test
    fun `content of screenDock is not displayed throughout whole SignUp route`() = runTest {

        // 1: verify content of screenDock is not displayed in 'WelcomeScreen'
        buttonHome.assertIsNotDisplayed()
        buttonStudy.assertIsNotDisplayed()

        // 2: verify content of screenDock is not displayed in 'SignUpScreen'

            // navigate to 'SignUpScreen' screen
            buttonSignUp.performClick()
            ctr.awaitIdle()

            // verification
            buttonHome.assertIsNotDisplayed()
            buttonStudy.assertIsNotDisplayed()

        // 3: verify content of screenDock is not displayed in 'SignUpEmailInformationScreen'

            // navigate to 'SignUpEmailInformationScreen'
            emailTextField.performTextInput("google.user@gmail.com")
            ctr.awaitIdle()
            passwordTextField.performTextInput("1@Aaaaaaaaaaaaaaaa")
            ctr.awaitIdle()

            // verification
            buttonHome.assertIsNotDisplayed()
            buttonStudy.assertIsNotDisplayed()
    }

    @Test
    fun `screen dock appears automatically after a user signs in`() = runTest {

        // 1: sign in
        buttonSignIn.performClick()
        ctr.awaitIdle()

        emailTextField.performTextInput("google.user@gmail.com")
        ctr.awaitIdle()
        passwordTextField.performTextInput("1@Aaaaaaaaaaaaaaaa")
        ctr.awaitIdle()

        buttonSignIn.performClick()
        ctr.awaitIdle()

        // 2: verify content of screen dock is visible after a user signs in
        buttonHome.assertIsDisplayed()
        buttonStudy.assertIsDisplayed()
    }

    @Test
    fun `screenDock works as intended`() = runTest {

        // 1: sign in
        buttonSignIn.performClick()
        ctr.awaitIdle()
        emailTextField.performTextInput("google.user@gmail.com")
        ctr.awaitIdle()
        passwordTextField.performTextInput("1@Aaaaaaaaaaaaaaaa")
        buttonSignIn.performClick()
        ctr.awaitIdle()


        // 2: make sure screenDock and its content are displayed
        ctr.onNodeWithText("Home subscreen")

        screenDock.assertIsDisplayed()

        buttonHome.assertIsDisplayed()
        buttonHome.assertIsNotEnabled()

        buttonStudy.assertIsDisplayed()
        buttonStudy.assertIsEnabled()


        // 3: navigate to 'SubjectsSubscreen'
        buttonStudy.performClick()
        ctr.awaitIdle()

            // verify state has been properly changed
            ctr.onNodeWithText("Subjects subscreen")
            buttonHome.assertIsEnabled()
            buttonStudy.assertIsNotEnabled()


        // 4: navigate to 'HomeSubscreen'
        buttonHome.performClick()

            // verify state has been properly changed
            ctr.onNodeWithText("Home subscreen")
            buttonHome.assertIsNotEnabled()
            buttonStudy.assertIsEnabled()



        // 5: navigate back to 'SubjectsSubscreen' with in-built back button
        ctr.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

            // verify state has been properly changed
            ctr.onNodeWithText("Subjects subscreen")
            buttonHome.assertIsEnabled()
            buttonStudy.assertIsNotEnabled()
    }

    // WelcomeScreen
    private val buttonSignIn = ctr.onNodeWithTag("button 'Sign in.'")
    private val buttonSignUp = ctr.onNodeWithTag("button 'Sign up.'")

    // SignInScreen
    private val emailTextField = ctr.onNodeWithTag("emailTextField")
    private val passwordTextField = ctr.onNodeWithTag("passwordTextField")
    private val buttonRecoverYourPassword = ctr.onNodeWithTag("button 'Recover your password.'")

    // ScreenDock
    private val screenDock = ctr.onNodeWithTag("screenDock")
    private val buttonHome = ctr.onNodeWithTag("screenDockButton 'home'")
    private val buttonStudy = ctr.onNodeWithTag("screenDockButton 'study'")
}