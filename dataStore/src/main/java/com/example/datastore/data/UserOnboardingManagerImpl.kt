package com.example.datastore.data

import androidx.datastore.core.DataStore
import com.example.datastore.model.UserOnboardingState
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException
import javax.inject.Inject

internal class UserOnboardingManagerImpl @Inject constructor(
    private val dataStoreUserOnboardingState : DataStore<UserOnboardingState>
): UserOnboardingManager {

    override suspend fun checkOnboardingState(): UserOnboardingState? {

        return try {

            this.dataStoreUserOnboardingState
                .data
                .firstOrNull()
        }

        catch (e: IOException) { null }
    }

    override suspend fun setOnboardingStateToFalse() {

        dataStoreUserOnboardingState.updateData {
            it.copy(firstLaunchEver = false)
        }
    }
}