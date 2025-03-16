package eu.project.aiesla.auth.authenticationManager

import app.cash.turbine.test
import eu.project.aiesla.auth.authentication.FirebaseAuthentication
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.results.*
import eu.project.aiesla.testHelpers.MainDispatcherRule
import io.mockk.*
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class ProductionAuthenticationManagerTest {

    private lateinit var firebaseAuthentication: FirebaseAuthentication

    private lateinit var authenticationManager: AuthenticationManager

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun before() {

        MockKAnnotations.init(this, relaxUnitFun = true)
        firebaseAuthentication = mockk(relaxed = true)

        authenticationManager = ProductionAuthenticationManager(
            firebaseAuthentication = firebaseAuthentication,
            coroutineScope = CoroutineScope(StandardTestDispatcher())
        )
    }

    @After
    fun after() {

        clearMocks(firebaseAuthentication)
        unmockkAll()
    }



    // isSignedIn
    @Test
    fun `isSignedIn - returns true when user is signed in`() = runTest {

        coEvery { firebaseAuthentication.isSignedIn() } returns true

        assertTrue(authenticationManager.isSignedIn())

        coVerify(atLeast = 1, atMost = 1) {

            firebaseAuthentication.isSignedIn()
        }
    }

    @Test
    fun `isSignedIn - returns false when user is not signed in`() = runTest {

        coEvery { firebaseAuthentication.isSignedIn() } returns false

        assertFalse(authenticationManager.isSignedIn())

        coVerify(atLeast = 1, atMost = 1) {

            firebaseAuthentication.isSignedIn()
        }
    }



    // signIn
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

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword("email", "password") } returns ResultOfSignInProcess.Ok
        authenticationManager.signIn(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signInWithEmailAndPassword(any(), any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - InvalidEmailFormat when ResultOfSignInProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword("email", "password") } returns ResultOfSignInProcess.InvalidEmailFormat
        authenticationManager.signIn(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signInWithEmailAndPassword(any(), any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - PasswordIsIncorrect when ResultOfSignInProcess is PasswordIsIncorrect`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword("email", "password") } returns ResultOfSignInProcess.PasswordIsIncorrect
        authenticationManager.signIn(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signInWithEmailAndPassword(any(), any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - UnidentifiedException when ResultOfSignInProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword("email", "password") } returns ResultOfSignInProcess.UnidentifiedException
        authenticationManager.signIn(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signInWithEmailAndPassword(any(), any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - Timeout when it takes more than 10 seconds to sign in`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword("email", "password") } coAnswers {

            delay(10500)
            ResultOfSignInProcess.Ok
        }

        authenticationManager.signIn(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signInWithEmailAndPassword(any(), any())
        }
    }
}