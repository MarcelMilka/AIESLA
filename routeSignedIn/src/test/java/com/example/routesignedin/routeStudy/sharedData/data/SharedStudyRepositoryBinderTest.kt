package com.example.routesignedin.routeStudy.sharedData.data

import com.example.databases.common.DatabaseManager
import com.example.databases.common.StateOfDataInitialization
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SharedStudyRepositoryBinderTest {

    private val roomLocalDatabaseManager = mockk<DatabaseManager>()
    private val stateOfDataInitialization = MutableStateFlow(StateOfDataInitialization.Idle)

    private lateinit var sharedStudyRepositoryBinder: SharedStudyRepositoryBinder

    @Before
    fun before() {

        sharedStudyRepositoryBinder = SharedStudyRepositoryBinder(
            roomLocalDatabaseManager = roomLocalDatabaseManager
        )
    }



    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is Idle, dataMustBeInitialized is true - initializeData is called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns true
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 1) {

            roomLocalDatabaseManager.initializeData()
        }
    }

    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is Idle, dataMustBeInitialized is false - initializeData is not called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns false
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 0) {

            roomLocalDatabaseManager.initializeData()
        }
    }

    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is Idle, dataMustBeInitialized is null - initializeData is not called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns null
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 0) {

            roomLocalDatabaseManager.initializeData()
        }
    }

    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is not Idle, dataMustBeInitialized is true - initializeData is not called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        stateOfDataInitialization.emit(StateOfDataInitialization.Pending)

        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns true
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 0) {

            roomLocalDatabaseManager.initializeData()
        }
    }

    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is not Idle, dataMustBeInitialized is false - initializeData is not called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        stateOfDataInitialization.emit(StateOfDataInitialization.Pending)

        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns false
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 0) {

            roomLocalDatabaseManager.initializeData()
        }
    }

    @Test
    fun `checkIfLocalDatabaseMustBeInitialized - StateOfDataInitialization is not Idle, dataMustBeInitialized is null - initializeData is not called`() = runTest {

        // stubbing
        every { roomLocalDatabaseManager.stateOfDataInitialization } returns stateOfDataInitialization
        stateOfDataInitialization.emit(StateOfDataInitialization.Pending)

        coEvery { roomLocalDatabaseManager.dataMustBeInitialized() } returns null
        coEvery { roomLocalDatabaseManager.initializeData() } just Runs

        // testing
        sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()

        // verification
        coVerify(exactly = 0) {

            roomLocalDatabaseManager.initializeData()
        }
    }
}