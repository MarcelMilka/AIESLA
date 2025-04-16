package com.example.routesignedout.routeSignIn.recoverYourPassword.ui

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.InstrumentationRegistry
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.results.PasswordRecoveryProcess
import com.example.authentication.results.UnsuccessfulPasswordRecoveryCause
import com.example.routesignedout.routeSignIn.recoverYourPassword.vm.RecoverYourPasswordScreenViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.sharedui.R

class ComposeRecoverYourPasswordScreenTest {

    @get:Rule(order = 0)
    val ctr = createComposeRule()

    // Dependencies
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var passwordRecoveryProcess: MutableStateFlow<PasswordRecoveryProcess>

    private lateinit var recoverYourPasswordScreenViewModel: RecoverYourPasswordScreenViewModel

    // RecoverYourPasswordScreen
    private val recoverYourPasswordScreen = ctr.onNodeWithTag("RecoverYourPasswordScreen")

    private val emailTextField = ctr.onNodeWithTag("RecoverYourPasswordScreen emailTextField")
    private val emailTextFieldHintImpl = ctr.onNodeWithTag("EmailTextFieldHint impl")
    private val emailTextFieldHint = ctr.onNodeWithTag("EmailTextFieldHint")

    private val buttonRecoverYourPassword = ctr.onNodeWithTag("RecoverYourPasswordScreen `recover your password`")
    private val iconCheck = ctr.onNodeWithContentDescription("icon 'check'")
    private val iconClose = ctr.onNodeWithContentDescription("icon 'close'")



    @Before
    fun before() {

        authenticationManager = mockk(relaxed = true)
        passwordRecoveryProcess = MutableStateFlow(PasswordRecoveryProcess.Idle)
        every { authenticationManager.passwordRecoveryProcess } returns passwordRecoveryProcess

        recoverYourPasswordScreenViewModel = RecoverYourPasswordScreenViewModel(authenticationManager)

        ctr.setContent {

            recoverYourPasswordScreen(
                email = recoverYourPasswordScreenViewModel.email.collectAsState().value,
                onUpdateEmail = { recoverYourPasswordScreenViewModel.updateEmail(it) },
                emailHintViewState = recoverYourPasswordScreenViewModel.stateOfEmailHint.collectAsState().value,
                buttonProceedViewState = recoverYourPasswordScreenViewModel.stateOfButtonProceed.collectAsState().value,
                onRecoverPassword = {}
            )
        }
    }

    @Test
    fun `make sure all default elements are displayed`() = runTest {

        recoverYourPasswordScreen.assertIsDisplayed()
        emailTextField.assertIsDisplayed()

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

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()
    }

    @Test
    fun `email text field - hides InvalidEmailFormat hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
        ctr.onNodeWithText(getResourceString(R.string.invalid_email_address)).assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays Timeout`() = runTest {

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.the_process_took_too_long)).onFirst().assertIsDisplayed()
    }

    @Test
    fun `email text field - hides Timeout hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.Timeout))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.the_process_took_too_long)).onFirst().assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }

    @Test
    fun `email text field - displays UnidentifiedException`() = runTest {

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onFirst().assertIsDisplayed()
    }

    @Test
    fun `email text field - hides UnidentifiedException hint after updating the email`() = runTest {

        emailTextField.performTextInput("email")

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.UnidentifiedException))

        emailTextFieldHintImpl.assertIsDisplayed()
        emailTextFieldHint.assertIsDisplayed()
        ctr.onAllNodesWithText(getResourceString(R.string.unidentified_error)).onFirst().assertIsDisplayed()

        emailTextField.performTextInput(".")

        emailTextFieldHintImpl.assertIsNotDisplayed()
        emailTextFieldHint.assertIsNotDisplayed()
    }



    // BUTTON RECOVER YOUR PASSWORD
    @Test
    fun `button recover your password - is enabled once email is not empty`() = runTest {

        buttonRecoverYourPassword.assertIsNotEnabled()

        emailTextField.performTextInput("x")

        buttonRecoverYourPassword.assertIsEnabled()
    }

    @Test
    fun `button recover your password - changes its state finishing successfully`() = runTest {

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Pending)
        buttonRecoverYourPassword.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Successful)
        buttonRecoverYourPassword.assertIsNotEnabled()
        iconCheck.assertIsDisplayed()
    }

    @Test
    fun `button recover your password - changes its state finishing unsuccessfully`() = runTest {

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Pending)
        buttonRecoverYourPassword.assertIsNotEnabled()
        ctr.onNodeWithText("...").assertIsDisplayed()

        passwordRecoveryProcess.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat))
        buttonRecoverYourPassword.assertIsNotEnabled()
        iconClose.assertIsDisplayed()
    }
}

private fun getResourceString(id: Int): String {
    val targetContext: Context = InstrumentationRegistry.getTargetContext()
    return targetContext.resources.getString(id)
}