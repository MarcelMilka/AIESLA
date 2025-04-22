package com.example.databases.roomLocalDatabase

import com.example.databases.common.DatabaseManager
import com.example.databases.common.ResultOfDataInitialization
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
    fun `initializeData - methods initializeMetadata and setOnboardingStateToFalse are properly executed - Successful ResultOfDataInitialization is returned`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } just Runs
        coEvery { userOnboardingManager.setOnboardingStateToFalse() } just Runs

        // testing
        assertEquals(
            ResultOfDataInitialization.Successful,
            databaseManager.initializeData()
        )

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `initializeData - initializeMetadata throws error - Unsuccessful ResultOfDataInitialization is returned`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } throws RuntimeException()

        // testing
        assertEquals(
            ResultOfDataInitialization.Unsuccessful,
            databaseManager.initializeData()
        )

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
        }
        confirmVerified()
        checkUnnecessaryStub()
    }

    @Test
    fun `initializeData - setOnboardingStateToFalse throws error - Unsuccessful ResultOfDataInitialization is returned`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = metadataEntity) } just Runs
        coEvery { userOnboardingManager.setOnboardingStateToFalse() } throws RuntimeException()

        // testing
        assertEquals(
            ResultOfDataInitialization.Unsuccessful,
            databaseManager.initializeData()
        )

        // verifying
        coVerify(exactly = 1) {

            metadataDAO.initializeMetadata(metadataEntity = metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()
        }
        confirmVerified()
        checkUnnecessaryStub()
    }
}