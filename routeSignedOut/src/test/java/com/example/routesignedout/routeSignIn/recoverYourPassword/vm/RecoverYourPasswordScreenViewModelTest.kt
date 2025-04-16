package com.example.routesignedout.routeSignIn.recoverYourPassword.vm

import app.cash.turbine.test
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.PasswordRecoveryProcess
import com.example.authentication.results.UnsuccessfulPasswordRecoveryCause
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintOptions
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintViewState
import com.example.routesignedout.testHelpers.MainDispatcherRule
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RecoverYourPasswordScreenViewModelTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var passwordRecoveryFlow: MutableStateFlow<PasswordRecoveryProcess>

    private lateinit var viewModel: RecoverYourPasswordScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        passwordRecoveryFlow = MutableStateFlow(PasswordRecoveryProcess.Idle)
        authenticationManager = mockk(relaxed = true)

        every { authenticationManager.passwordRecoveryProcess } returns passwordRecoveryFlow

        viewModel = RecoverYourPasswordScreenViewModel(authenticationManager)
    }

    @After
    fun after() {

        clearMocks(authenticationManager)
        unmockkAll()
    }



    @Test
    fun `updateEmail works as intended`() = runTest(StandardTestDispatcher()) {

        viewModel.updateEmail(EmailCredential("test@example.com"))

        assertEquals("test@example.com", viewModel.email.value.email)
    }

    @Test
    fun `getEmail returns current email`() = runTest(StandardTestDispatcher()) {

        viewModel.updateEmail(EmailCredential("example@mail.com"))

        assertEquals(EmailCredential("example@mail.com"), viewModel.getEmail())
    }

    @Test
    fun `sendPasswordRecoveryEmail works properly, calls sendPasswordRecoveryEmail only once`() = runTest(
        StandardTestDispatcher()
    ) {

        viewModel.sendPasswordRecoveryEmail()

        coVerify (exactly = 1){

            authenticationManager.sendPasswordRecoveryEmail(any())
        }
    }



    @Test
    fun `stateOfButtonProceed - is Disabled by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.passwordRecoveryProcess } returns passwordRecoveryFlow

        viewModel.stateOfButtonProceed.test {

            assertEquals(ButtonProceedViewState.Disabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when email changes`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering email
            viewModel.updateEmail(EmailCredential("e"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Disabled when email is empty
            viewModel.updateEmail(EmailCredential(""))
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after re-entering email
            viewModel.updateEmail(EmailCredential("e"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when password recovery process is Pending
            launch { delay(1000); passwordRecoveryFlow.emit(PasswordRecoveryProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Successful when password recovery process is Successful
            launch { delay(200); passwordRecoveryFlow.emit(PasswordRecoveryProcess.Successful) }
            assertEquals(ButtonProceedViewState.Successful, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when stateOfEmailHint changes`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering email
            viewModel.updateEmail(EmailCredential("e"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when password recovery process is Pending
            launch { delay(1000); passwordRecoveryFlow.emit(PasswordRecoveryProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful(Email-related error)
            launch { delay(200); passwordRecoveryFlow.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but email hint is visible
            launch { delay(200); passwordRecoveryFlow.emit(PasswordRecoveryProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when password recovery process is Idle and email hint is not visible
            viewModel.updateEmail(EmailCredential("em"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }



    @Test
    fun `stateOfEmailHint - is Invisible by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.passwordRecoveryProcess } returns passwordRecoveryFlow

        viewModel.stateOfEmailHint.test {

            assertEquals(SignInEmailHintViewState.Invisible, awaitItem())
        }
    }

    @Test
    fun `stateOfEmailHint is properly changed`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.passwordRecoveryProcess } returns passwordRecoveryFlow

        viewModel.stateOfEmailHint.test {

            // default
            assertEquals(SignInEmailHintViewState.Invisible, awaitItem())

            // Email error
            launch { passwordRecoveryFlow.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat)) }
            assertEquals(SignInEmailHintViewState.Visible(SignInEmailHintOptions.InvalidEmailFormat), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email"))
            assertEquals(SignInEmailHintViewState.Invisible, awaitItem())

            // Timeout error
            launch { passwordRecoveryFlow.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.Timeout)) }
            assertEquals(SignInEmailHintViewState.Visible(SignInEmailHintOptions.Timeout), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email"))
            assertEquals(SignInEmailHintViewState.Invisible, awaitItem())

            // Unidentified error
            launch { passwordRecoveryFlow.emit(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.UnidentifiedException)) }
            assertEquals(SignInEmailHintViewState.Visible(SignInEmailHintOptions.UnidentifiedException), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email email"))
            assertEquals(SignInEmailHintViewState.Invisible, awaitItem())
        }
    }
}