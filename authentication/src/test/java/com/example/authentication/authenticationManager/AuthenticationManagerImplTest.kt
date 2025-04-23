package com.example.authentication.authenticationManager

import app.cash.turbine.test
import com.example.authentication.authentication.Authentication
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.*
import com.example.datastore.data.UserOnboardingManager
import com.example.datastore.model.UserOnboardingState
import io.mockk.*
import junit.framework.TestCase.assertEquals
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
class
AuthenticationManagerImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userOnboardingManagerImpl: UserOnboardingManager
    private lateinit var firebaseAuthentication: Authentication
    private lateinit var roomAuthentication: Authentication

    private lateinit var authenticationManager: AuthenticationManager

    @Before
    fun before() {

        userOnboardingManagerImpl = mockk()
        firebaseAuthentication = mockk()
        roomAuthentication = mockk()
    }

    @After
    fun after() {

        clearMocks(userOnboardingManagerImpl)
        clearMocks(firebaseAuthentication)
        clearMocks(roomAuthentication)
        unmockkAll()
    }

    private val emailAndPasswordCredentials = EmailAndPasswordCredentials("email", "password")



    @Test
    fun `checkAuthenticationState - UserOnboardingState is not null, firstLaunchEver is true - InitialAuthenticationState is SignedIn`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = true)

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is not null, firstLaunchEver is false, firebase signedIn is true  - InitialAuthenticationState is SignedIn`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = false)
        every { firebaseAuthentication.isSignedIn() } returns true

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        verify (exactly = 1) { firebaseAuthentication.isSignedIn() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is not null, firstLaunchEver is false, firebase signedIn is false, room signedIn is true  - InitialAuthenticationState is SignedIn`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = false)
        every { firebaseAuthentication.isSignedIn() } returns false
        every { roomAuthentication.isSignedIn() } returns true

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.SignedIn, this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        verify (exactly = 1) {

            firebaseAuthentication.isSignedIn()
            roomAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is not null, firstLaunchEver is null - InitialAuthenticationState is Unsuccessful Null`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = null)

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - UserOnboardingState is null - InitialAuthenticationState is Unsuccessful Null`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns null

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Null), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - process takes more than 10 seconds - InitialAuthenticationState is Unsuccessful Timeout`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } coAnswers {

            delay(10500)
            null
        }

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.Timeout), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - checkOnboardingState throws Exception - InitialAuthenticationState is Unsuccessful UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } throws RuntimeException()

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.UnidentifiedException), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - firebase isSignedIn throws Exception - InitialAuthenticationState is Unsuccessful UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = false)
        every { firebaseAuthentication.isSignedIn() } throws  RuntimeException()

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.UnidentifiedException), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        coVerify(exactly = 1) { firebaseAuthentication.isSignedIn() }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `checkAuthenticationState - room isSignedIn throws Exception - InitialAuthenticationState is Unsuccessful UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
        coEvery { userOnboardingManagerImpl.checkOnboardingState() } returns UserOnboardingState(firstLaunchEver = false)
        every { firebaseAuthentication.isSignedIn() } returns false
        every { roomAuthentication.isSignedIn() } throws RuntimeException()

        // testing
        authenticationManager.initialAuthenticationState.test {

            assertEquals(InitialAuthenticationState.SignedOut, this.awaitItem())

            assertEquals(InitialAuthenticationState.Unsuccessful(UnsuccessfulInitializationCause.UnidentifiedException), this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) { userOnboardingManagerImpl.checkOnboardingState() }
        coVerify(exactly = 1) {

            firebaseAuthentication.isSignedIn()
            roomAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }



    @Test
    fun `signUp - signUpProcess is Idle by default`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        authenticationManager.signUpProcess.test {

            assertEquals(SignUpProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Successful when ResultOfSignUpProcess is Ok and ResultOfSendingSignUpVerificationEmail is Ok`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        // stubbing
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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        authenticationManager.signInProcess.test {

            assertEquals(SignInProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Successful when ResultOfSignInProcess is Ok`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Idle by default`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

        authenticationManager.passwordRecoveryProcess.test {

            assertEquals(PasswordRecoveryProcess.Idle, this.awaitItem())

            this.cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sendPasswordRecoveryEmail - passwordRecoveryProcess is Successful when ResultOfPasswordRecoveryProcess is Ok`() = runTest(StandardTestDispatcher()) {

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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

        // initialization
        authenticationManager = AuthenticationManagerImpl(
            userOnboardingManager = userOnboardingManagerImpl,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = this
        )

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