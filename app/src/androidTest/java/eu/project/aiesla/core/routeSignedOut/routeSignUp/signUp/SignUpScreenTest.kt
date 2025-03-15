package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.input.ImeAction
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
class SignUpScreenTestAndroid {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val acr = createAndroidComposeRule<MainActivity>()

    // WelcomeScreen
    private val signUpButton = acr.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign up.'")

    // SignUpScreen
    private val emailTextField = acr.onNodeWithTag("SignUpScreen emailTextField")

    private val passwordTextField = acr.onNodeWithTag("SignUpScreen passwordTextField")
    private val passwordTextFieldIconButton = acr.onNodeWithTag("PasswordTextField icon button")
    private val visibilityOnIconButton = acr.onNodeWithContentDescription("Icon 'visibility on'")
    private val visibilityOffIconButton = acr.onNodeWithContentDescription("Icon 'visibility off'")

    private val passwordRequirementTotalCharacters = acr.onNodeWithTag("SignUpScreen passwordRequirement 'At least x characters'")
    private val passwordRequirementUppercaseCharacter = acr.onNodeWithTag("SignUpScreen passwordRequirement 'At least x uppercase characters'")
    private val passwordRequirementSpecialCharacter = acr.onNodeWithTag("SignUpScreen passwordRequirement 'At least x special character'")
    private val passwordRequirementNumericCharacter = acr.onNodeWithTag("SignUpScreen passwordRequirement 'At least x numeric character'")
    
    private val dynamicAuthenticationButton = acr.onNodeWithTag("SignUpScreen dynamicAuthenticationButton")

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Before
    fun before() {

        hiltRule.inject()

        // navigate from 'WelcomeScreen' to 'SignUpScreen'
        signUpButton.performClick()
    }

    @Test
    fun `emailTextField is focused automatically after entering the screen`() = runTest {

        // make sure the keyboard is visible and emailTextFieldIsFocused
        acr.activityRule.scenario.onActivity {
            it.window.decorView.rootWindowInsets.isVisible(android.view.WindowInsets.Type.ime())
        }

        emailTextField.assertIsFocused()
    }

    @Test
    fun `clicking the keyboard button next focuses on passwordTextField`() = runTest {

        // click the button 'Next'
        acr.onNode(hasImeAction(ImeAction.Next)).performImeAction()

        // make sure the passwordTextField is focused
        passwordTextField.assertIsFocused()
    }

    @Test
    fun `all password hints are displayed after focusing on the passwordTextField`() = runTest {

        // make sure the password hints are not displayed when passwordTextField is not focused
        passwordRequirementTotalCharacters.assertIsNotDisplayed()
        passwordRequirementUppercaseCharacter.assertIsNotDisplayed()
        passwordRequirementSpecialCharacter.assertIsNotDisplayed()
        passwordRequirementNumericCharacter.assertIsNotDisplayed()

        // focus on the passwordTextField
        acr.onNode(hasImeAction(ImeAction.Next)).performImeAction()

        // make sure the password hints are displayed when passwordTextField is focused
        emailTextField.assertIsNotFocused()
        passwordTextField.assertIsFocused()

        passwordRequirementTotalCharacters.assertIsDisplayed()
        passwordRequirementUppercaseCharacter.assertIsDisplayed()
        passwordRequirementSpecialCharacter.assertIsDisplayed()
        passwordRequirementNumericCharacter.assertIsDisplayed()

        // make sure once again the password hints are not displayed when passwordTextField is not focused
        emailTextField.performClick()

        emailTextField.assertIsFocused()
        passwordTextField.assertIsNotFocused()

        passwordRequirementTotalCharacters.assertIsNotDisplayed()
        passwordRequirementUppercaseCharacter.assertIsNotDisplayed()
        passwordRequirementSpecialCharacter.assertIsNotDisplayed()
        passwordRequirementNumericCharacter.assertIsNotDisplayed()

        // make sure once again the password hints are displayed when passwordTextField is focused
        passwordTextField.performClick()

        emailTextField.assertIsNotFocused()
        passwordTextField.assertIsFocused()

        passwordRequirementTotalCharacters.assertIsDisplayed()
        passwordRequirementUppercaseCharacter.assertIsDisplayed()
        passwordRequirementSpecialCharacter.assertIsDisplayed()
        passwordRequirementNumericCharacter.assertIsDisplayed()
    }

    @Test
    fun `button hide show password works as intended`() = runTest {

        // focus on the passwordTextField
        acr.onNode(hasImeAction(ImeAction.Next)).performImeAction()

        // insert some random value
        passwordTextField.performTextInput("aaa")

        // make sure PasswordVisualTransformation is active
        acr.onNodeWithText("•••").assertIsDisplayed()
        visibilityOnIconButton.assertIsNotDisplayed()
        visibilityOffIconButton.assertIsDisplayed()

        // make sure actual password is displayed after clicking the 'visibility on' button
        passwordTextFieldIconButton.performClick()
        acr.onNodeWithText("aaa").assertIsDisplayed()
        visibilityOnIconButton.assertIsDisplayed()
        visibilityOffIconButton.assertIsNotDisplayed()

        // insert some random value again and verify displayed value
        passwordTextField.performTextInput(" bbb")
        acr.onNodeWithText("aaa bbb").assertIsDisplayed()
        visibilityOnIconButton.assertIsDisplayed()
        visibilityOffIconButton.assertIsNotDisplayed()

        // hide inserted test
        passwordTextFieldIconButton.performClick()
        acr.onNodeWithText("•••••••").assertIsDisplayed()
        visibilityOnIconButton.assertIsNotDisplayed()
        visibilityOffIconButton.assertIsDisplayed()
    }

    @Test
    fun `button register is deactivated by default`() = runTest {

        dynamicAuthenticationButton.assertIsDisplayed()
        dynamicAuthenticationButton.assertIsNotEnabled()
    }

    @Test
    fun `button register is activated immediately once all credential requirements get fulfilled`() = runTest {

        // insert credentials
        emailTextField.performTextInput("google.account@gmail.com")
        dynamicAuthenticationButton.assertIsNotEnabled()

        acr.onNode(hasImeAction(ImeAction.Next)).performImeAction()

        passwordTextField.performTextInput("A#1abcde")

        // make sure the button is enabled
        dynamicAuthenticationButton.assertIsEnabled()
    }
}