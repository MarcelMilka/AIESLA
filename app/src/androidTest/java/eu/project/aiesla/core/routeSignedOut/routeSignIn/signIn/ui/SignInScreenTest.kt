package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.ui

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import androidx.test.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.project.aiesla.R
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.auth.results.UnsuccessfulSignInProcessCause
import eu.project.aiesla.core.MainActivity
import eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.vm.SignInScreenViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// AndroidSignInScreenTest used to test ime behavior
// ComposeSignInScreenTest used to test rest of the behavior

@HiltAndroidTest
class AndroidSignInScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val acr = createAndroidComposeRule<MainActivity>()

    // SignInScreen
    private val signInScreen = acr.onNodeWithTag("WelcomeScreen primaryAuthenticationTextButton 'Sign in.'")

    private val emailTextField = acr.onNodeWithTag("SignInScreen emailTextField")

    private val passwordTextField = acr.onNodeWithTag("SignInScreen passwordTextField")


    @Before
    fun before() {

        hiltRule.inject()

        // navigate from 'WelcomeScreen' to 'SignUpScreen'
        signInScreen.performClick()
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

        // make sure the passwordTextField is focused and keyboard is visible
        passwordTextField.assertIsFocused()

        acr.activityRule.scenario.onActivity {
            it.window.decorView.rootWindowInsets.isVisible(android.view.WindowInsets.Type.ime())
        }

    }
}

class ComposeSignInScreenTest {

    @get:Rule(order = 0)
    val ctr = createComposeRule()

    // Dependencies
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var signInFlow: MutableStateFlow<SignInProcess>

    private lateinit var signInScreenViewModel: SignInScreenViewModel

    // SignInScreen
    private val signInScreen = ctr.onNodeWithTag("SignInScreen")

    private val emailTextField = ctr.onNodeWithTag("SignInScreen emailTextField")
    private val emailTextFieldHintImpl = ctr.onNodeWithTag("EmailTextFieldHint impl")
    private val emailTextFieldHint = ctr.onNodeWithTag("EmailTextFieldHint")

    private val passwordTextField = ctr.onNodeWithTag("SignInScreen passwordTextField")

    private val passwordTextFieldButton = ctr.onNodeWithTag("PasswordTextField icon button")
    private val iconVisibilityOn = ctr.onNodeWithContentDescription("Icon 'visibility on'")
    private val iconVisibilityOff = ctr.onNodeWithContentDescription("Icon 'visibility off'")

    private val passwordTextFieldHintImpl = ctr.onNodeWithTag("PasswordTextFieldHint impl")
    private val passwordTextFieldHint = ctr.onNodeWithTag("PasswordTextFieldHint")

    private val buttonSignIn = ctr.onNodeWithTag("SignInScreen button 'Sign in'")
    private val iconCheck = ctr.onNodeWithContentDescription("icon 'check'")
    private val iconClose = ctr.onNodeWithContentDescription("icon 'close'")


    private val buttonRecoverYourPassword = ctr.onNodeWithTag("SignInScreen button 'Recover your password")



    @Before
    fun before() {

        authenticationManager = mockk(relaxed = true)
        signInFlow = MutableStateFlow(SignInProcess.Idle)
        every { authenticationManager.signInProcess } returns signInFlow

        signInScreenViewModel = SignInScreenViewModel(authenticationManager)

        ctr.setContent {

            signInScreen(
                credentials = signInScreenViewModel.credentials.collectAsState().value,
                onUpdateEmail = { signInScreenViewModel.updateEmail(it) },
                onUpdatePassword = { signInScreenViewModel.updatePassword(it) },
                emailHintViewState = signInScreenViewModel.stateOfEmailHint.collectAsState().value,
                passwordHintViewState = signInScreenViewModel.stateOfPasswordHint.collectAsState().value,
                buttonProceedViewState = signInScreenViewModel.stateOfButtonProceed.collectAsState().value,
                onSignIn = {},
                onRecoverPassword = {}
            )
        }
    }

    @Test
    fun `make sure all default elements are displayed`() = runTest {

        signInScreen.assertIsDisplayed()
        emailTextField.assertIsDisplayed()

        passwordTextFieldButton.assertIsDisplayed()
        iconVisibilityOff.assertIsDisplayed()

        passwordTextField.assertIsDisplayed()
        buttonSignIn.assertIsDisplayed()
        buttonRecoverYourPassword.assertIsDisplayed()
    }



    // EMAIL TEXT FIELD
    @Test
    fun `email text field - is focused by default`() = runTest {

        emailTextField.assertIsFocused()
    }

    @Test
    fun `email text field - displays appropriate entered credential`() = runTest {

        emailTextField.performTextInput("e")
        ctr.onNodeWithText("e").assertIsDisplayed()

        emailTextField.performTextInput("m")
        ctr.onNodeWithText("em").assertIsDisplayed()

        emailTextField.performTextInput("a")
        ctr.onNodeWithText("ema").assertIsDisplayed()

        emailTextField.performTextInput("i")
        ctr.onNodeWithText("emai").assertIsDisplayed()

        emailTextField.performTextInput("l")
        ctr.onNodeWithText("email").assertIsDisplayed()
    }

    @Test
    fun `email text field - displays InvalidEmailFormat`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides InvalidEmailFormat hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsNotDisplayed()
    }

    @Test
    fun `email text field - does not display PasswordIsIncorrect`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect))

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `email text field - does not hide PasswordIsIncorrect hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect))

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
    }

    @Test
    fun `email text field - displays Timeout`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.timeout)).onFirst().assertIsDisplayed()
    }

    @Test
    fun `email text field - hides Timeout hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.timeout)).onFirst().assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays UnidentifiedException`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onFirst().assertIsDisplayed()
    }

    @Test
    fun `email text field - hides UnidentifiedException hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onFirst().assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }



    // PASSWORD TEXT FIELD
    @Test
    fun `password text field - displays hidden password by default`() = runTest {

        passwordTextField.performTextInput("A")
        ctr.onNodeWithText("•").assertIsDisplayed()

        passwordTextField.performTextInput("B")
        ctr.onNodeWithText("••").assertIsDisplayed()

        passwordTextField.performTextInput("C")
        ctr.onNodeWithText("•••").assertIsDisplayed()


//        passwordTextField.performTextInput("p")
//        ctr.onNodeWithText("p").assertIsDisplayed()
//
//        passwordTextField.performTextInput("a")
//        ctr.onNodeWithText("pa").assertIsDisplayed()
//
//        passwordTextField.performTextInput("s")
//        ctr.onNodeWithText("pas").assertIsDisplayed()
//
//        passwordTextField.performTextInput("s")
//        ctr.onNodeWithText("pass").assertIsDisplayed()
//
//        passwordTextField.performTextInput("w")
//        ctr.onNodeWithText("passw").assertIsDisplayed()
//
//        passwordTextField.performTextInput("o")
//        ctr.onNodeWithText("passwo").assertIsDisplayed()
//
//        passwordTextField.performTextInput("r")
//        ctr.onNodeWithText("passwor").assertIsDisplayed()
//
//        passwordTextField.performTextInput("d")
//        ctr.onNodeWithText("passwod").assertIsDisplayed()
    }

    @Test
    fun `password text field - displays password after clicking the button show and vice versa`() = runTest {

        // insert some password to be hidden
        passwordTextField.performTextInput("Password")
        ctr.onNodeWithText("••••••••").assertIsDisplayed()

        // show the password
        iconVisibilityOff.assertIsDisplayed()
        iconVisibilityOn.assertIsNotDisplayed()

        passwordTextFieldButton.performClick()

        iconVisibilityOff.assertIsNotDisplayed()
        iconVisibilityOn.assertIsDisplayed()

        ctr.onNodeWithText("Password").assertIsDisplayed()

        // insert some password to be displayed
        passwordTextField.performTextInput("###")
        ctr.onNodeWithText("Password###").assertIsDisplayed()

        // hide the password
        passwordTextFieldButton.performClick()

        iconVisibilityOff.assertIsDisplayed()
        iconVisibilityOn.assertIsNotDisplayed()

        ctr.onNodeWithText("•••••••••••").assertIsDisplayed()
    }

    @Test
    fun `password text field - does not display InvalidEmailFormat`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat))

        passwordTextFieldHintImpl.assertIsNotDisplayed()
        passwordTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - does not hide InvalidEmailFormat hint after updating the password`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat))

        passwordTextField.performTextInput("Password")

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
    }

    @Test
    fun `password text field - displays PasswordIsIncorrect`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_password)).assertIsDisplayed()
    }

    @Test
    fun `password text field - hides PasswordIsIncorrect hint after updating the password`() = runTest {

        passwordTextField.performTextInput("password")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_password)).assertIsDisplayed()

        passwordTextField.performTextInput(".")

        passwordTextFieldHintImpl.assertIsNotDisplayed()
        passwordTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - displays Timeout`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.timeout)).onLast().assertIsDisplayed()
    }

    @Test
    fun `password text field - hides Timeout hint after updating the password`() = runTest {

        passwordTextField.performTextInput("password")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.timeout)).onLast().assertIsDisplayed()

        passwordTextField.performTextInput(".")

        passwordTextFieldHintImpl.assertIsNotDisplayed()
        passwordTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - displays UnidentifiedException`() = runTest {

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onLast().assertIsDisplayed()
    }

    @Test
    fun `password text field - hides UnidentifiedException hint after updating the password`() = runTest {

        passwordTextField.performTextInput("password")

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException))

        passwordTextFieldHintImpl.assertIsDisplayed()
        passwordTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onLast().assertIsDisplayed()

        passwordTextField.performTextInput(".")

        passwordTextFieldHintImpl.assertIsNotDisplayed()
        passwordTextFieldHint.assertIsNotDisplayed()
    }



    // BUTTON SIGN IN
    @Test
    fun `button sign in - is disabled by default`() = runTest {

        buttonSignIn.assertIsNotEnabled()
    }

    @Test
    fun `button sign in - is disabled when only email is not empty`() = runTest {

        buttonSignIn.assertIsNotEnabled()

        emailTextField.performTextInput("x")

        buttonSignIn.assertIsNotEnabled()
    }

    @Test
    fun `button sign in - is disabled when only password is not empty`() = runTest {

        buttonSignIn.assertIsNotEnabled()

        emailTextField.performTextInput("x")

        buttonSignIn.assertIsNotEnabled()
    }

    @Test
    fun `button sign in - is enabled once both email and password are not empty`() = runTest {

        buttonSignIn.assertIsNotEnabled()

        emailTextField.performTextInput("x")
        passwordTextField.performTextInput("x")

        buttonSignIn.assertIsEnabled()
    }

    @Test
    fun `button sign in - changes its state finishing successfully`() = runTest {

        signInFlow.emit(SignInProcess.Pending)
        buttonSignIn.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        signInFlow.emit(SignInProcess.Successful)
        buttonSignIn.assertIsNotEnabled()
        iconCheck.assertIsDisplayed()
    }

    @Test
    fun `button sign in - changes its state finishing unsuccessfully`() = runTest {

        signInFlow.emit(SignInProcess.Pending)
        buttonSignIn.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout))
        buttonSignIn.assertIsNotEnabled()
        iconClose.assertIsDisplayed()
    }



    // BUTTON RECOVER YOUR PASSWORD
    @Test
    fun `button recover your password is enabled`() = runTest {

        buttonRecoverYourPassword.assertIsEnabled()
    }
}

private fun getResourceString(id: Int): String {
    val targetContext: Context = InstrumentationRegistry.getTargetContext()
    return targetContext.resources.getString(id)
}