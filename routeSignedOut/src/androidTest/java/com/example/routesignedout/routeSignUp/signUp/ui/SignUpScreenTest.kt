package com.example.routesignedout.routeSignUp.signUp.ui

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.InstrumentationRegistry
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.PasswordCredential
import com.example.authentication.credentials.PasswordRequirements
import com.example.authentication.results.SignUpProcess
import com.example.authentication.results.UnsuccessfulSignUpProcessCause
import com.example.routesignedout.routeSignUp.signUp.vm.SignUpScreenViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.sharedui.R

class SignUpScreenTest {

    @get:Rule(order = 0)
    val ctr = createComposeRule()

    // Dependencies
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var signUpFlow: MutableStateFlow<SignUpProcess>

    private lateinit var viewModel: SignUpScreenViewModel

    // SignInScreen
    private val signUpScreen = ctr.onNodeWithTag("SignUpScreen")

    private val emailTextField = ctr.onNodeWithTag("SignUpScreen - emailTextField")
    private val emailTextFieldHintImpl = ctr.onNodeWithTag("SignUpEmailTextFieldHint impl")
    private val emailTextFieldHint = ctr.onNodeWithTag("SignUpEmailTextFieldHint")

    private val passwordTextField = ctr.onNodeWithTag("SignUpScreen - passwordTextField")

    private val passwordTextFieldButton = ctr.onNodeWithTag("PasswordTextField - icon button")
    private val iconVisibilityOn = ctr.onNodeWithContentDescription("Icon 'visibility on'")
    private val iconVisibilityOff = ctr.onNodeWithContentDescription("Icon 'visibility off'")

    private val passwordTextFieldHintImpl = ctr.onNodeWithTag("SignUpPasswordTextFieldHint impl")
    private val totalCharactersHint = ctr.onNodeWithTag("totalCharacters")
    private val uppercaseCharacters = ctr.onNodeWithTag("uppercaseCharacters")
    private val specialCharacters = ctr.onNodeWithTag("specialCharacters")
    private val numericCharacters = ctr.onNodeWithTag("numericCharacters")

    private val buttonSignIn = ctr.onNodeWithTag("SignUpScreen button 'Sign up'")
    private val iconCheck = ctr.onNodeWithContentDescription("icon 'check'")
    private val iconClose = ctr.onNodeWithContentDescription("icon 'close'")



    @Before
    fun before() {

        authenticationManager = mockk(relaxed = true)
        signUpFlow = MutableStateFlow(SignUpProcess.Idle)
        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel = SignUpScreenViewModel(authenticationManager)

        ctr.setContent {

            signUpScreen(
                credentials = viewModel.credentials.collectAsState().value,
                onUpdateEmail = { viewModel.updateEmail(it) },
                onUpdatePassword = { viewModel.updatePassword(it) },
                onFocusChanged = { viewModel.reactToOnFocusChanged(it) },
                emailHintViewState = viewModel.stateOfEmailHint.collectAsState().value,
                passwordHintViewState = viewModel.stateOfPasswordHint.collectAsState().value,
                buttonProceedViewState = viewModel.stateOfButtonProceed.collectAsState().value,
                onSignUp = { viewModel.signUp() },
            )
        }
    }

    @Test
    fun `make sure all default elements are displayed`() = runTest {

        signUpScreen.assertIsDisplayed()
        emailTextField.assertIsDisplayed()

        passwordTextFieldButton.assertIsDisplayed()
        iconVisibilityOff.assertIsDisplayed()

        passwordTextField.assertIsDisplayed()
        buttonSignIn.assertIsDisplayed()
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

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides InvalidEmailFormat hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays EmailIsAlreadyInUse`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.email_is_already_in_use)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides EmailIsAlreadyInUse hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.email_is_already_in_use)).assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.email_is_already_in_use)).assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays Timeout`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.the_process_took_too_long)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides Timeout hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.the_process_took_too_long)).assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays UnidentifiedException`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.unidentified_error)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides UnidentifiedException hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.unidentified_error)).assertIsDisplayed()

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

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat))

        passwordTextFieldHintImpl.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - does not hide InvalidEmailFormat hint after updating the password`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat))

        passwordTextField.performTextInput("Password")

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
    }

    @Test
    fun `password text field - displays PasswordIsIncorrect`() = runTest {}

    @Test
    fun `password text field - hides PasswordIsIncorrect hint after updating the password`() = runTest {}

    @Test
    fun `password text field - does not display Timeout`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout))

        passwordTextFieldHintImpl.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - does not hide Timeout hint after updating the password`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout))

        passwordTextField.performTextInput("Password")

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
    }

    @Test
    fun `password text field - does not display UnidentifiedException`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException))

        passwordTextFieldHintImpl.assertIsNotDisplayed()
    }

    @Test
    fun `password text field - does not hide UnidentifiedException hint after updating the password`() = runTest {

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException))

        passwordTextField.performTextInput("Password")

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
    }

    @Test
    fun `password text field - displays appropriate password hints`() = runTest {

        // focus on password text field
        passwordTextField.performClick()
        totalCharactersHint.assertIsDisplayed()
        uppercaseCharacters.assertIsDisplayed()
        specialCharacters.assertIsDisplayed()
        numericCharacters.assertIsDisplayed()

        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${getResourceString(R.string.characters)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${getResourceString(R.string.uppercase_letters)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${getResourceString(R.string.special_character)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${getResourceString(R.string.numeric_character)}").assertIsDisplayed()

        // verification #1
        viewModel.updatePassword(PasswordCredential("P"))
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${getResourceString(R.string.characters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${getResourceString(R.string.uppercase_letters)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${getResourceString(R.string.special_character)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${getResourceString(R.string.numeric_character)}").assertIsDisplayed()

        // verification #2
        viewModel.updatePassword(PasswordCredential("Password"))
        ctr.onNodeWithText("8/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${getResourceString(R.string.characters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${getResourceString(R.string.uppercase_letters)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${getResourceString(R.string.special_character)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${getResourceString(R.string.numeric_character)}").assertIsDisplayed()

        // verification #3
        viewModel.updatePassword(PasswordCredential("Password!"))
        ctr.onNodeWithText("9/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${getResourceString(R.string.characters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${getResourceString(R.string.uppercase_letters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${getResourceString(R.string.special_character)}").assertIsDisplayed()
        ctr.onNodeWithText("0/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${getResourceString(R.string.numeric_character)}").assertIsDisplayed()

        // verification #4
        viewModel.updatePassword(PasswordCredential("Password!9"))
        ctr.onNodeWithText("10/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${getResourceString(R.string.characters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${getResourceString(R.string.uppercase_letters)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${getResourceString(R.string.special_character)}").assertIsDisplayed()
        ctr.onNodeWithText("1/${getResourceString(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${getResourceString(R.string.numeric_character)}").assertIsDisplayed()
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
        passwordTextField.performTextInput("1A!catDog")

        buttonSignIn.assertIsEnabled()
    }

    @Test
    fun `button sign in - changes its state finishing successfully`() = runTest {

        signUpFlow.emit(SignUpProcess.Pending)
        buttonSignIn.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        signUpFlow.emit(SignUpProcess.Successful)
        buttonSignIn.assertIsNotEnabled()
        iconCheck.assertIsDisplayed()
    }

    @Test
    fun `button sign in - changes its state finishing unsuccessfully`() = runTest {

        signUpFlow.emit(SignUpProcess.Pending)
        buttonSignIn.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout))
        buttonSignIn.assertIsNotEnabled()
        iconClose.assertIsDisplayed()
    }
}

private fun getResourceString(id: Int): String {
    val targetContext: Context = InstrumentationRegistry.getTargetContext()
    return targetContext.resources.getString(id)
}