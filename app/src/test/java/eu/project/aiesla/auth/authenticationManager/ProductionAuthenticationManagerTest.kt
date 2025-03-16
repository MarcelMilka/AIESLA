package eu.project.aiesla.auth.authenticationManager

import app.cash.turbine.test
import eu.project.aiesla.auth.authentication.FirebaseAuthentication
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
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
        coEvery { firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignInProcess.Ok
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

            firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - InvalidEmailFormat when ResultOfSignInProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignInProcess.InvalidEmailFormat
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

            firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - PasswordIsIncorrect when ResultOfSignInProcess is PasswordIsIncorrect`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignInProcess.PasswordIsIncorrect
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

            firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - UnidentifiedException when ResultOfSignInProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignInProcess.UnidentifiedException
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

            firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signIn - signInProcess is Unsuccessful - Timeout when it takes more than 10 seconds to sign in`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } coAnswers {

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

            firebaseAuthentication.signInWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }



    // signUp
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

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignUpProcess.Ok
        coEvery { firebaseAuthentication.sendSignUpVerificationEmail() } returns ResultOfSendingSignUpVerificationEmail.Ok

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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
        coVerify(exactly = 1) {

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
            firebaseAuthentication.sendSignUpVerificationEmail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is Unsuccessful - UnidentifiedException when ResultOfSignUpProcess is Ok and ResultOfSendingSignUpVerificationEmail is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignUpProcess.Ok
        coEvery { firebaseAuthentication.sendSignUpVerificationEmail() } returns ResultOfSendingSignUpVerificationEmail.UnidentifiedException

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
            firebaseAuthentication.sendSignUpVerificationEmail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is is Unsuccessful - InvalidEmailFormat when ResultOfSignUpProcess is InvalidEmailFormat`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignUpProcess.InvalidEmailFormat

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is is EmailIsAlreadyInUse - InvalidEmailFormat when ResultOfSignUpProcess is EmailIsAlreadyInUse`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignUpProcess.EmailIsAlreadyInUse

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is is UnidentifiedException - InvalidEmailFormat when ResultOfSignUpProcess is UnidentifiedException`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } returns ResultOfSignUpProcess.UnidentifiedException

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signUp - signUpProcess is is Unsuccessful - Timeout when it takes more than 10 seconds to sign in`() = runTest(StandardTestDispatcher()) {

        // set up the test
        coEvery { firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password")) } coAnswers {

            delay(10500)
            ResultOfSignUpProcess.Ok
        }

        authenticationManager.signUp(credentials = EmailAndPasswordCredentials("email", "password"))

        // test
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

            firebaseAuthentication.signUpWithEmailAndPassword(EmailAndPasswordCredentials("email", "password"))
        }
    }



    // sendPasswordRecoveryEmail
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