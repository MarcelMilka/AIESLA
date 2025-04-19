package com.example.databases.dataSynchronizationManager

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DataSynchronizationManagerImplTest {

    private val manager = DataSynchronizationManagerImpl()

    @Test
    fun `changeDataSynchronizationState updates state flow correctly`() = runTest {
        manager.dataSynchronizationState.test {

            // idle by default
            assertEquals(DataSynchronizationState.Idle, awaitItem())

            // syncing
            manager.changeDataSynchronizationState(DataSynchronizationState.Synchronizing)
            assertEquals(DataSynchronizationState.Synchronizing, awaitItem())

            // Change state to Completed
            manager.changeDataSynchronizationState(DataSynchronizationState.Synchronized)
            assertEquals(DataSynchronizationState.Synchronized, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}