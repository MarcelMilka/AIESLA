package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.vm

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.credentials.PasswordCredential
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.auth.results.SignInProcess
import eu.project.aiesla.auth.results.SignUpProcess
import eu.project.aiesla.auth.results.UnsuccessfulSignInProcessCause
import eu.project.aiesla.sharedUi.sharedElements.button.ButtonProceedViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.EmailTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.EmailTextFieldViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.PasswordTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.PasswordTextFieldViewState
import eu.project.aiesla.testHelpers.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class SignInScreenViewModelTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var signInFlow: MutableStateFlow<SignInProcess>

    private lateinit var viewModel: SignInScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        signInFlow = MutableStateFlow(SignInProcess.Idle)
        authenticationManager = mockk(relaxed = true)

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel = SignInScreenViewModel(authenticationManager)
    }

    @After
    fun after() {

        clearMocks(authenticationManager)
        unmockkAll()
    }



    @Test
    fun `updateEmail updates credentials`() = runTest(StandardTestDispatcher()) {
        viewModel.updateEmail(EmailCredential("test@example.com"))
        assertEquals("test@example.com", viewModel.credentials.value.email)
    }

    @Test
    fun `updatePassword updates credentials`() = runTest(StandardTestDispatcher()) {
        viewModel.updatePassword(PasswordCredential("securePassword123"))
        assertEquals("securePassword123", viewModel.credentials.value.password)
    }

    @Test
    fun `updateEmail and updatePassword update credentials`() = runTest(StandardTestDispatcher()) {
        viewModel.updateEmail(EmailCredential("user@test.com"))
        viewModel.updatePassword(PasswordCredential("strongPass123"))

        assertEquals("user@test.com", viewModel.credentials.value.email)
        assertEquals("strongPass123", viewModel.credentials.value.password)
    }

    @Test
    fun `getCredentials returns current values`() = runTest(StandardTestDispatcher()) {
        viewModel.updateEmail(EmailCredential("example@mail.com"))
        viewModel.updatePassword(PasswordCredential("password456"))

        assertEquals(EmailAndPasswordCredentials("example@mail.com", "password456"), viewModel.getCredentials())
    }

    @Test
    fun `signIn works properly, calls signIn only once`() = runTest(StandardTestDispatcher()) {

        viewModel.signIn()

        coVerify (exactly = 1){

            authenticationManager.signIn(any())
        }
    }




    @Test
    fun `stateOfButtonProceed - is Disabled by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel.stateOfButtonProceed.test {

            assertEquals(ButtonProceedViewState.Disabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when credentials change`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Disabled when email is empty
            viewModel.updateEmail(EmailCredential(""))
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after re-entering email
            viewModel.updateEmail(EmailCredential("e"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Disabled when password is empty
            viewModel.updatePassword(PasswordCredential(""))
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after re-entering password
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Disabled when credentials are empty
            viewModel.updateEmail(EmailCredential(""))
            viewModel.updatePassword(PasswordCredential(""))
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signInFlow.emit(SignInProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Successful when sign-in process is Successful
            launch { delay(200); signInFlow.emit(SignInProcess.Successful) }
            assertEquals(ButtonProceedViewState.Successful, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when stateOfEmailHint changes`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signInFlow.emit(SignInProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful(Email-related error)
            launch { delay(200); signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but email hint is visible
            launch { delay(200); signInFlow.emit(SignInProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when sign-in process is Idle and email hint is not visible
            viewModel.updateEmail(EmailCredential("em"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when stateOfPasswordHint changes`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signInFlow.emit(SignInProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful(Password-related error)
            launch { delay(200); signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but password hint is visible
            launch { delay(200); signInFlow.emit(SignInProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when sign-in process is Idle and email hint is not visible
            viewModel.updatePassword(PasswordCredential("pa"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when stateOfEmailHint and stateOfPasswordHint change - UnidentifiedException case`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signInFlow.emit(SignInProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful
            launch { delay(200); signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but email and password hints are visible
            launch { delay(200); signInFlow.emit(SignInProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when sign-in process is Idle, email and password hints are not visible
            viewModel.updateEmail(EmailCredential("email"))
            viewModel.updatePassword(PasswordCredential("password"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when stateOfEmailHint and stateOfPasswordHint change - Timeout case`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("p"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signInFlow.emit(SignInProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful
            launch { delay(200); signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but email and password hints are visible
            launch { delay(200); signInFlow.emit(SignInProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when sign-in process is Idle, email and password hints are not visible
            viewModel.updateEmail(EmailCredential("email"))
            viewModel.updatePassword(PasswordCredential("password"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }



    @Test
    fun `stateOfEmailHint - is Invisible by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel.stateOfEmailHint.test {

            assertEquals(EmailTextFieldViewState.Invisible, awaitItem())
        }
    }

    @Test
    fun `stateOfEmailHint is properly changed`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel.stateOfEmailHint.test {

            // default
            assertEquals(EmailTextFieldViewState.Invisible, awaitItem())

            // Email error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat)) }
            assertEquals(EmailTextFieldViewState.Visible(EmailTextFieldHint.InvalidEmailFormat), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email"))
            assertEquals(EmailTextFieldViewState.Invisible, awaitItem())

            // Timeout error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout)) }
            assertEquals(EmailTextFieldViewState.Visible(EmailTextFieldHint.Timeout), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email"))
            assertEquals(EmailTextFieldViewState.Invisible, awaitItem())

            // Unidentified error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException)) }
            assertEquals(EmailTextFieldViewState.Visible(EmailTextFieldHint.UnidentifiedException), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email email"))
            assertEquals(EmailTextFieldViewState.Invisible, awaitItem())
        }
    }



    @Test
    fun `stateOfPasswordHint - is Invisible by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel.stateOfPasswordHint.test {

            assertEquals(PasswordTextFieldViewState.Invisible, awaitItem())
        }
    }

    @Test
    fun `stateOfPasswordHint is properly changed`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signInProcess } returns signInFlow

        viewModel.stateOfPasswordHint.test {

            // default
            assertEquals(PasswordTextFieldViewState.Invisible, awaitItem())

            // Password error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect)) }
            assertEquals(PasswordTextFieldViewState.Visible(PasswordTextFieldHint.PasswordIsIncorrect), awaitItem())

            // Hide via entering email
            viewModel.updatePassword(PasswordCredential("password"))
            assertEquals(PasswordTextFieldViewState.Invisible, awaitItem())

            // Timeout error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout)) }
            assertEquals(PasswordTextFieldViewState.Visible(PasswordTextFieldHint.Timeout), awaitItem())

            // Hide via entering email
            viewModel.updatePassword(PasswordCredential("password"))
            assertEquals(PasswordTextFieldViewState.Invisible, awaitItem())


            // Unidentified error
            launch { signInFlow.emit(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException)) }
            assertEquals(PasswordTextFieldViewState.Visible(PasswordTextFieldHint.UnidentifiedException), awaitItem())

            // Hide via entering email
            viewModel.updatePassword(PasswordCredential("password"))
            assertEquals(PasswordTextFieldViewState.Invisible, awaitItem())
        }
    }
}