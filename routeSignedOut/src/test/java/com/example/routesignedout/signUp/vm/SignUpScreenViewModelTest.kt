package com.example.routesignedout.signUp.vm

import app.cash.turbine.test
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.credentials.PasswordCredential
import com.example.authentication.results.SignUpProcess
import com.example.authentication.results.UnsuccessfulSignUpProcessCause
import com.example.routesignedout.routeSignUp.signUp.model.PasswordRequirementViewState
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintOptions
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintViewState
import com.example.routesignedout.routeSignUp.signUp.model.SignUpPasswordHintViewState
import com.example.routesignedout.routeSignUp.signUp.vm.SignUpScreenViewModel
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
class SignUpScreenViewModelTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var signUpFlow: MutableStateFlow<SignUpProcess>

    private lateinit var viewModel: SignUpScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun before() {

        MockKAnnotations.init(this, relaxUnitFun = true)

        signUpFlow = MutableStateFlow(SignUpProcess.Idle)
        authenticationManager = mockk(relaxed = true)

        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel = SignUpScreenViewModel(authenticationManager)
    }

    @After
    fun after() {

        clearMocks(authenticationManager)
        unmockkAll()
    }



    @Test
    fun `updateEmail works as intended`() = runTest(StandardTestDispatcher()) {
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
    fun `reactToOnFocusChanged works as intended`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfPasswordHint.test {

            // default
            assertEquals(SignUpPasswordHintViewState.Invisible, awaitItem())

            // visible
            launch { viewModel.reactToOnFocusChanged(true) }
            assertEquals(SignUpPasswordHintViewState.Visible(), awaitItem())

            // invisible
            launch { viewModel.reactToOnFocusChanged(false) }
            assertEquals(SignUpPasswordHintViewState.Invisible, awaitItem())

            // visible
            launch { viewModel.reactToOnFocusChanged(true) }
            assertEquals(SignUpPasswordHintViewState.Visible(), awaitItem())
        }
    }

    @Test
    fun `signUp works properly, calls signUp only once`() = runTest(StandardTestDispatcher()) {

        viewModel.signUp()

        coVerify (exactly = 1){

            authenticationManager.signUp(any())
        }
    }



    @Test
    fun `stateOfButtonProceed - is Disabled by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel.stateOfButtonProceed.test {

            assertEquals(ButtonProceedViewState.Disabled, awaitItem())
        }
    }

    @Test
    fun `stateOfButtonProceed is properly changed when credentials change`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfButtonProceed.test {

            // Disabled by default
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering email and password
            viewModel.updateEmail(EmailCredential("email.google@gmail.com"))
            viewModel.updatePassword(PasswordCredential("P1!assword"))
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
            viewModel.updatePassword(PasswordCredential("P1!assword"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Disabled when credentials are empty
            viewModel.updateEmail(EmailCredential(""))
            viewModel.updatePassword(PasswordCredential(""))
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled after entering credentials
            viewModel.updateEmail(EmailCredential("e"))
            viewModel.updatePassword(PasswordCredential("P1!assword"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signUpFlow.emit(SignUpProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Successful when sign-up process is Successful
            launch { delay(200); signUpFlow.emit(SignUpProcess.Successful) }
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
            viewModel.updatePassword(PasswordCredential("P1!assword"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())

            // Pending when sign in process is Pending
            launch { delay(1000); signUpFlow.emit(SignUpProcess.Pending) }
            assertEquals(ButtonProceedViewState.Pending, awaitItem())

            // Unsuccessful when sign-in process is Unsuccessful(Email-related error)
            launch { delay(200); signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat)) }
            assertEquals(ButtonProceedViewState.Unsuccessful, awaitItem())

            // Disabled when sign-in process is Idle but email hint is visible
            launch { delay(200); signUpFlow.emit(SignUpProcess.Idle) }
            assertEquals(ButtonProceedViewState.Disabled, awaitItem())

            // Enabled when sign-up process is Idle and email hint is not visible
            viewModel.updateEmail(EmailCredential("em"))
            assertEquals(ButtonProceedViewState.Enabled, awaitItem())
        }
    }


    @Test
    fun `stateOfEmailHint - is Invisible by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel.stateOfEmailHint.test {

            assertEquals(SignUpEmailHintViewState.Invisible, awaitItem())
        }
    }

    @Test
    fun `stateOfEmailHint is properly changed`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel.stateOfEmailHint.test {

            // default
            assertEquals(SignUpEmailHintViewState.Invisible, awaitItem())

            // Email error
            launch { signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat)) }
            assertEquals(SignUpEmailHintViewState.Visible(SignUpEmailHintOptions.InvalidEmailFormat), awaitItem())

            // Email error 2
            launch { signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse)) }
            assertEquals(SignUpEmailHintViewState.Visible(SignUpEmailHintOptions.EmailIsAlreadyInUse), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email"))
            assertEquals(SignUpEmailHintViewState.Invisible, awaitItem())

            // Timeout error
            launch { signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout)) }
            assertEquals(SignUpEmailHintViewState.Visible(SignUpEmailHintOptions.Timeout), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email"))
            assertEquals(SignUpEmailHintViewState.Invisible, awaitItem())

            // Unidentified error
            launch { signUpFlow.emit(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException)) }
            assertEquals(SignUpEmailHintViewState.Visible(SignUpEmailHintOptions.UnidentifiedException), awaitItem())

            // Hide via entering email
            viewModel.updateEmail(EmailCredential("email email email"))
            assertEquals(SignUpEmailHintViewState.Invisible, awaitItem())
        }
    }



    @Test
    fun `stateOfPasswordHint - is Invisible by default`() = runTest(StandardTestDispatcher()) {

        every { authenticationManager.signUpProcess } returns signUpFlow

        viewModel.stateOfPasswordHint.test {

            assertEquals(SignUpPasswordHintViewState.Invisible, awaitItem())
        }
    }

    @Test
    fun `stateOfPasswordHint is properly changed`() = runTest(StandardTestDispatcher()) {

        viewModel.stateOfPasswordHint.test {

            // default
            assertEquals(SignUpPasswordHintViewState.Invisible, awaitItem())

            // visible #1
            viewModel.reactToOnFocusChanged(true)
            viewModel.updatePassword(PasswordCredential("P1!assword"))

            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    )
                ),
                awaitItem()
            )

            // visible #2
            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 10
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    )
                ),
                awaitItem()
            )

            // visible #3
            viewModel.updatePassword(PasswordCredential("P1!assw"))

            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 7
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    )
                ),
                awaitItem()
            )

            // visible #4
            viewModel.updatePassword(PasswordCredential("1!assw"))

            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 6
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    )
                ),
                awaitItem()
            )

            // visible #5
            viewModel.updatePassword(PasswordCredential("!assw"))

            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 5
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = true,
                        currentCount = 1
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    )
                ),
                awaitItem()
            )

            // visible #6
            viewModel.updatePassword(PasswordCredential("assw"))

            assertEquals(
                SignUpPasswordHintViewState.Visible(
                    totalCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 4
                    ),
                    uppercaseCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    specialCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    ),
                    numericCharacters = PasswordRequirementViewState(
                        isGreen = false,
                        currentCount = 0
                    )
                ),
                awaitItem()
            )

            // visible #6
            viewModel.reactToOnFocusChanged(false)

            assertEquals(SignUpPasswordHintViewState.Invisible, awaitItem())
        }
    }
}