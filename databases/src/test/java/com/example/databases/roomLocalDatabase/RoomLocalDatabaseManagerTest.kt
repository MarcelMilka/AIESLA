package com.example.databases.roomLocalDatabase

import app.cash.turbine.test
import com.example.databases.common.DatabaseManager
import com.example.databases.common.StateOfDataInitialization
import com.example.datastore.data.UserOnboardingManager
import com.example.datastore.model.UserOnboardingState
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RoomLocalDatabaseManagerTest {

    private val userOnboardingManager = mockk<UserOnboardingManager>()
    private val metadataDAO = mockk<MetadataDAO>()

    private lateinit var databaseManager: DatabaseManager

    @Before
    fun before() {

        databaseManager = RoomLocalDatabaseManager(
            userOnboardingManager = userOnboardingManager,
            metadataDAO = metadataDAO
        )
    }

    private val metadataEntity = MetadataEntity(
        index = 0,
        relatedUUID = null,
        signedIn = true
    )

    @Test
    fun `dataMustBeInitialized - UserOnboardingState is not null, firstLaunchEver is true - true is returned`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(true)

        // testing
        assertEquals(
            true,
            databaseManager.dataMustBeInitialized()
        )

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `dataMustBeInitialized - UserOnboardingState is not null, firstLaunchEver is false - false is returned`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(false)

        // testing
        assertEquals(
            false,
            databaseManager.dataMustBeInitialized()
        )

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `dataMustBeInitialized - UserOnboardingState is not null, firstLaunchEver is null - null is returned`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(null)

        // testing
        assertNull(databaseManager.dataMustBeInitialized())

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `dataMustBeInitialized - UserOnboardingState is not null, firstLaunchEver is null - StateOfDataInitialization is Unsuccessful`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns UserOnboardingState(null)

        // testing
        databaseManager.stateOfDataInitialization.test {

            assertEquals(StateOfDataInitialization.Idle,this.awaitItem())

            databaseManager.dataMustBeInitialized()

            assertEquals(StateOfDataInitialization.Unsuccessful,this.awaitItem())

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
    fun `dataMustBeInitialized - UserOnboardingState is null - null is returned`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns null

        // testing
        assertNull(databaseManager.dataMustBeInitialized())

        // verifying
        coVerify(exactly = 1) {

            userOnboardingManager.checkOnboardingState()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `dataMustBeInitialized - UserOnboardingState is null - StateOfDataInitialization is Unsuccessful`() = runTest {

        // stubbing
        coEvery { userOnboardingManager.checkOnboardingState() } returns null

        // testing
        databaseManager.stateOfDataInitialization.test {

            assertEquals(StateOfDataInitialization.Idle,this.awaitItem())

            databaseManager.dataMustBeInitialized()

            assertEquals(StateOfDataInitialization.Unsuccessful,this.awaitItem())

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
    fun `initializeData - methods initializeMetadata and setOnboardingStateToFalse are properly executed - StateOfDataInitialization is Pending, then Successful`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } just Runs
        coEvery { userOnboardingManager.setOnboardingStateToFalse() } just Runs

        // testing
        databaseManager.stateOfDataInitialization.test {

            assertEquals(StateOfDataInitialization.Idle,this.awaitItem())

            databaseManager.initializeData()

            assertEquals(StateOfDataInitialization.Pending,this.awaitItem())

            assertEquals(StateOfDataInitialization.Successful,this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `initializeData - initializeMetadata throws error - StateOfDataInitialization is Pending, then Unsuccessful`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } throws RuntimeException()

        // testing
        databaseManager.stateOfDataInitialization.test {

            assertEquals(StateOfDataInitialization.Idle,this.awaitItem())

            databaseManager.initializeData()

            assertEquals(StateOfDataInitialization.Pending,this.awaitItem())

            assertEquals(StateOfDataInitialization.Unsuccessful,this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `initializeData - setOnboardingStateToFalse throws error - StateOfDataInitialization is Pending, then Unsuccessful`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } just Runs
        coEvery { userOnboardingManager.setOnboardingStateToFalse() } throws RuntimeException()

        // testing
        databaseManager.stateOfDataInitialization.test {

            assertEquals(StateOfDataInitialization.Idle,this.awaitItem())

            databaseManager.initializeData()

            assertEquals(StateOfDataInitialization.Pending,this.awaitItem())

            assertEquals(StateOfDataInitialization.Unsuccessful,this.awaitItem())

            this.ensureAllEventsConsumed()
        }

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }
}