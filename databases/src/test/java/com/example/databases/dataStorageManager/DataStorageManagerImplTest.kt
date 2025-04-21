package com.example.databases.dataStorageManager

import app.cash.turbine.test
import com.example.authentication.authentication.Authentication
import com.example.databases.dataSynchronizationManager.DataSynchronizationManager
import com.example.datastore.data.UserOnboardingManager
import com.example.datastore.model.UserOnboardingState
import com.example.networkconnectivity.NetworkConnectivityManager
import com.example.networkconnectivity.NetworkConnectivityStatus
import com.google.firebase.auth.FirebaseAuth
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DataStorageManagerImplTest {

    private lateinit var dataStorageManager: DataStorageManager

    private val networkConnectivityManager = mockk<NetworkConnectivityManager>()
    private val dataSynchronizationManager = mockk<DataSynchronizationManager>()
    private val userOnboardingManager = mockk<UserOnboardingManager>()
    private val firebaseAuthentication = mockk<Authentication>()
    private val roomAuthentication = mockk<Authentication>()
    private val firebaseAuth = mockk<FirebaseAuth>()

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is true - WayOfStoringData is Local`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(true)
        coEvery { userOnboardingManager.setOnboardingStateToFalse() } returns Unit

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
            userOnboardingManager.setOnboardingStateToFalse()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is false, Connected, firebase is signed in - WayOfStoringData is CloudAndLocal Connected`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(false)

        val networkConnectivityStatus = MutableStateFlow(NetworkConnectivityStatus.Connected).asStateFlow()
        coEvery { networkConnectivityManager.networkConnectivityStatus } returns networkConnectivityStatus

        every { firebaseAuthentication.isSignedIn() } returns true

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())

            assertEquals(WayOfStoringData.CloudAndLocal(networkConnectivityStatus= NetworkConnectivityStatus.Connected), awaitItem())
            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
            networkConnectivityManager.networkConnectivityStatus
            firebaseAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is false, Disconnected, firebase is signed in - WayOfStoringData is CloudAndLocal Disconnected`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(false)

        val networkConnectivityStatus = MutableStateFlow(NetworkConnectivityStatus.Disconnected).asStateFlow()
        coEvery { networkConnectivityManager.networkConnectivityStatus } returns networkConnectivityStatus

        every { firebaseAuthentication.isSignedIn() } returns true

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())

            assertEquals(WayOfStoringData.CloudAndLocal(networkConnectivityStatus= NetworkConnectivityStatus.Disconnected), awaitItem())
            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
            networkConnectivityManager.networkConnectivityStatus
            firebaseAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is false, ConnectionLost, firebase is signed in - WayOfStoringData is CloudAndLocal ConnectionLost`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(false)

        val networkConnectivityStatus = MutableStateFlow(NetworkConnectivityStatus.ConnectionLost).asStateFlow()
        coEvery { networkConnectivityManager.networkConnectivityStatus } returns networkConnectivityStatus

        every { firebaseAuthentication.isSignedIn() } returns true

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())

            assertEquals(WayOfStoringData.CloudAndLocal(networkConnectivityStatus= NetworkConnectivityStatus.ConnectionLost), awaitItem())
            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
            networkConnectivityManager.networkConnectivityStatus
            firebaseAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is false, ConnectionLost, firebase is signed out - WayOfStoringData is Local`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(false)
        every { firebaseAuthentication.isSignedIn() } returns false

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())
            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
            firebaseAuthentication.isSignedIn()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is not null, firstLaunchEver is null - WayOfStoringData is Unidentified`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(null)

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())

            assertEquals(WayOfStoringData.Unidentified, awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `specifyWayOfStoringData - UserOnboardingState is null - WayOfStoringData is Unidentified`() = runTest(StandardTestDispatcher()) {

        // initialization
        dataStorageManager = DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = this
        )

        // mocking
        coEvery { userOnboardingManager.checkOnboardingState() } returns null

        // testing
        dataStorageManager.wayOfStoringData.test {

            advanceUntilIdle()
            assertEquals(WayOfStoringData.Local, awaitItem())

            assertEquals(WayOfStoringData.Unidentified, awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }
}