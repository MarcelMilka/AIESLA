package com.example.authentication.authenticationManager

import app.cash.turbine.test
import com.example.authentication.authentication.Authentication
import com.example.authentication.results.AuthenticationState
import com.example.authentication.results.UnsuccessfulInitializationCause
import com.example.datastore.data.OnboardingRepository
import com.example.datastore.model.UserOnboardingState
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
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
}