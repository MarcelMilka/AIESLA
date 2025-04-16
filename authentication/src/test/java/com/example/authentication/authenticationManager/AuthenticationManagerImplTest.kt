package com.example.authentication.authenticationManager

import app.cash.turbine.test
import com.example.authentication.authentication.Authentication
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.*
import com.example.datastore.data.OnboardingRepository
import com.example.datastore.model.UserOnboardingState
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthenticationManagerImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var onboardingRepository: OnboardingRepository
    private lateinit var firebaseAuthentication: Authentication
    private lateinit var roomAuthentication: Authentication

    private lateinit var authenticationManager: AuthenticationManager

    @Before
    fun before() {

        onboardingRepository = mockk()
        firebaseAuthentication = mockk()
        roomAuthentication = mockk()

        authenticationManager = AuthenticationManagerImpl(
            onboardingRepository = onboardingRepository,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = CoroutineScope(StandardTestDispatcher())
        )
    }

    @After
    fun after() {

        clearMocks(firebaseAuthentication)
        unmockkAll()
    }

    private val emailAndPasswordCredentials = EmailAndPasswordCredentials("email", "password")

    @Test
    fun `checkAuthenticationState - UserOnboardingState is true - SignedIn`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns UserOnboardingState(true)

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerify(exactly = 1) { onboardingRepository.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is false, firebaseAuthentication is true - SignedIn`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns UserOnboardingState(false)
        every { firebaseAuthentication.isSignedIn() } returns true

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
            firebaseAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is false, firebaseAuthentication is false, roomAuthentication is true - SignedIn`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns UserOnboardingState(false)
        every { firebaseAuthentication.isSignedIn() } returns false
        every { roomAuthentication.isSignedIn() } returns true

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
            firebaseAuthentication.isSignedIn()
            roomAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is false, firebaseAuthentication is false, roomAuthentication is false - SignedOut`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns UserOnboardingState(false)
        every { firebaseAuthentication.isSignedIn() } returns false
        every { roomAuthentication.isSignedIn() } returns false

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            this.ensureAllEventsConsumed()
        }
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is null - UnsuccessfulInitializationCause, Null`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns null

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState contains null - UnsuccessfulInitializationCause, Null`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } returns UserOnboardingState(null)

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - takes more than 10s - UnsuccessfulInitializationCause, UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } coAnswers {

            delay(10500)
            UserOnboardingState(true)
        }

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Timeout), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - throws RuntimeException - UnsuccessfulInitializationCause, UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { onboardingRepository.checkOnboardingState() } throws RuntimeException("")

        // test
        authenticationManager.authenticationState.test {

            assertEquals(AuthenticationState.SignedOut, this.awaitItem())

            assertEquals(AuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.UnidentifiedException), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verification
        coVerifySequence {

            onboardingRepository.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }



    @Test
    fun `signUp - signUpProcess is Idle by default`() = runTest {

        authenticationManager.signUpProcess.test {

            assertEquals(SignUpProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Successful when ResultOfSignUpProcess is Ok and ResultOfSendingSignUpVerificationEmail is Ok`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signUp(credentials = any()) } returns ResultOfSignUpProcess.Ok
        coEvery { firebaseAuthentication.sendSignUpVerificationEmail() } returns ResultOfSendingSignUpVerificationEmail.Ok

        // testing
        authenticationManager.signUp(emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Successful, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerifySequence {

            firebaseAuthentication.signUp(credentials = any())
            firebaseAuthentication.sendSignUpVerificationEmail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Unsuccessful - UnidentifiedException when ResultOfSignUpProcess is Ok and ResultOfSendingSignUpVerificationEmail is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signUp(credentials = any()) } returns ResultOfSignUpProcess.Ok
        coEvery { firebaseAuthentication.sendSignUpVerificationEmail() } returns ResultOfSendingSignUpVerificationEmail.UnidentifiedException

        // testing
        authenticationManager.signUp(credentials = emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerifySequence {

            firebaseAuthentication.signUp(credentials = any())
            firebaseAuthentication.sendSignUpVerificationEmail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Unsuccessful - InvalidEmailFormat when ResultOfSignUpProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signUp(credentials = any()) } returns ResultOfSignUpProcess.InvalidEmailFormat

        // testing
        authenticationManager.signUp(credentials = emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.InvalidEmailFormat), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signUp(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is EmailIsAlreadyInUse - InvalidEmailFormat when ResultOfSignUpProcess is EmailIsAlreadyInUse`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUp(credentials = any()) } returns ResultOfSignUpProcess.EmailIsAlreadyInUse

        // testing
        authenticationManager.signUp(credentials = emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signUp(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is UnidentifiedException - InvalidEmailFormat when ResultOfSignUpProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signUp(credentials = any()) } returns ResultOfSignUpProcess.UnidentifiedException

        // testing
        authenticationManager.signUp(credentials = emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.UnidentifiedException), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signUp(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Unsuccessful - Timeout when it takes more than 10 seconds to sign in`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signUp(credentials = any()) } coAnswers {

            delay(10500)
            ResultOfSignUpProcess.Ok
        }

        // testing
        authenticationManager.signUp(credentials = emailAndPasswordCredentials)

        authenticationManager.signUpProcess.test {

            advanceUntilIdle()
            assertEquals(SignUpProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignUpProcess.Unsuccessful(UnsuccessfulSignUpProcessCause.Timeout), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signUp(credentials = any())
        }
    }



    @Test
    fun `signIn - signInProcess is Idle by default`() = runTest {

        authenticationManager.signInProcess.test {

            assertEquals(SignInProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Successful when ResultOfSignInProcess is Ok`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signIn(credentials = any()) } returns ResultOfSignInProcess.Ok

        // testing
        authenticationManager.signIn(credentials = emailAndPasswordCredentials)
        authenticationManager.signInProcess.test {

            advanceUntilIdle()
            assertEquals(SignInProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Successful, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signIn(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - InvalidEmailFormat when ResultOfSignInProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signIn(credentials = any()) } returns ResultOfSignInProcess.InvalidEmailFormat

        // testing
        authenticationManager.signIn(credentials = emailAndPasswordCredentials)
        authenticationManager.signInProcess.test {

            advanceUntilIdle()
            assertEquals(SignInProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.InvalidEmailFormat), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signIn(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - PasswordIsIncorrect when ResultOfSignInProcess is PasswordIsIncorrect`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signIn(credentials = any()) } returns ResultOfSignInProcess.PasswordIsIncorrect

        // testing
        authenticationManager.signIn(credentials = emailAndPasswordCredentials)
        authenticationManager.signInProcess.test {

            advanceUntilIdle()
            assertEquals(SignInProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.PasswordIsIncorrect), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signIn(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - UnidentifiedException when ResultOfSignInProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signIn(credentials = any()) } returns ResultOfSignInProcess.UnidentifiedException

        // testing
        authenticationManager.signIn(credentials = emailAndPasswordCredentials)
        authenticationManager.signInProcess.test {

            advanceUntilIdle()
            assertEquals(SignInProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.UnidentifiedException), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signIn(credentials = any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - Timeout when it takes more than 10 seconds to sign in`() = runTest(StandardTestDispatcher()) {

        // stubbing
        coEvery { firebaseAuthentication.signIn(credentials = any()) } coAnswers {

            delay(10500)
            ResultOfSignInProcess.Ok
        }

        // testing
        authenticationManager.signIn(credentials = emailAndPasswordCredentials)
        authenticationManager.signInProcess.test {

            advanceUntilIdle()
            assertEquals(SignInProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(SignInProcess.Unsuccessful(UnsuccessfulSignInProcessCause.Timeout), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {

            firebaseAuthentication.signIn(credentials = any())
        }
    }



    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Idle by default`() = runTest {

        authenticationManager.passwordRecoveryProcess.test {

            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Successful when ResultOfPasswordRecoveryProcess is Ok`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.sendPasswordRecoveryEmail(email = EmailCredential("email")) } returns ResultOfPasswordRecoveryProcess.Ok
        authenticationManager.sendPasswordRecoveryEmail(email = EmailCredential("email"))

        // test
        authenticationManager.passwordRecoveryProcess.test {

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Successful, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {
            firebaseAuthentication.sendPasswordRecoveryEmail(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Unsuccessful - InvalidEmailFormat when ResultOfPasswordRecoveryProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.sendPasswordRecoveryEmail(email = EmailCredential("email")) } returns ResultOfPasswordRecoveryProcess.InvalidEmailFormat
        authenticationManager.sendPasswordRecoveryEmail(email = EmailCredential("email"))

        // test
        authenticationManager.passwordRecoveryProcess.test {

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {
            firebaseAuthentication.sendPasswordRecoveryEmail(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Unsuccessful - UnidentifiedException when ResultOfPasswordRecoveryProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.sendPasswordRecoveryEmail(email = EmailCredential("email")) } returns ResultOfPasswordRecoveryProcess.UnidentifiedException
        authenticationManager.sendPasswordRecoveryEmail(email = EmailCredential("email"))

        // test
        authenticationManager.passwordRecoveryProcess.test {

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.UnidentifiedException), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {
            firebaseAuthentication.sendPasswordRecoveryEmail(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Unsuccessful - Timeout when it takes more than 10 seconds`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.sendPasswordRecoveryEmail(email = EmailCredential("email")) } coAnswers {

            delay(10500)
            ResultOfPasswordRecoveryProcess.Ok
        }

        authenticationManager.sendPasswordRecoveryEmail(email = EmailCredential("email"))

        // test
        authenticationManager.passwordRecoveryProcess.test {

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Pending, this.awaitItem())

            advanceUntilIdle()
            assertEquals(PasswordRecoveryProcess.Unsuccessful(UnsuccessfulPasswordRecoveryCause.Timeout), this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }

        // verification
        coVerify(exactly = 1) {
            firebaseAuthentication.sendPasswordRecoveryEmail(any())
        }
    }
}