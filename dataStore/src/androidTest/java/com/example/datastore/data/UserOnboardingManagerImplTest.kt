package com.example.datastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.test.core.app.ApplicationProvider
import com.example.datastore.model.UserOnboardingState
import com.example.datastore.model.UserOnboardingStateSerializer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserOnboardingManagerImplTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var testDataStore: DataStore<UserOnboardingState>

    private lateinit var repository: UserOnboardingManagerImpl

    @Before
    fun before() {

        testDataStore = DataStoreFactory
            .create(
                serializer = UserOnboardingStateSerializer,
                scope = testScope,
                produceFile = { testContext.filesDir.resolve("fake-user-onboarding-state.json") }
            )

        repository = UserOnboardingManagerImpl(testDataStore)
    }

    @After
    fun cleanup() {

        runTest {
            testContext.cacheDir.resolve("fake-user-onboarding-state.json").delete()
        }
    }



    @Test
    fun `checkOnboardingState - returns true by default`() = runTest(testDispatcher) {

        val expected = UserOnboardingState(true)
        val actual = repository.checkOnboardingState()

        assertEquals(expected, actual)
    }

    @Test
    fun `setOnboardingStateToFalse - sets onboarding state to false`() = runTest(testDispatcher) {

        // 1
        val expected = UserOnboardingState(true)
        val actual = repository.checkOnboardingState()

        assertEquals(expected, actual)

        // 2
        repository.setOnboardingStateToFalse()

        val expected2 = UserOnboardingState(false)
        val actual2 = repository.checkOnboardingState()

        assertEquals(expected2, actual2)
    }
}