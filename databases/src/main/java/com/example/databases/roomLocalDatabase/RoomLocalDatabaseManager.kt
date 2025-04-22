package com.example.databases.roomLocalDatabase

import com.example.databases.common.DatabaseManager
import com.example.databases.common.StateOfDataInitialization
import com.example.datastore.data.UserOnboardingManager
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class RoomLocalDatabaseManager @Inject constructor(
    val userOnboardingManager: UserOnboardingManager,
    val metadataDAO: MetadataDAO
): DatabaseManager {

    private var _stateOfDataInitialization = MutableStateFlow(StateOfDataInitialization.Idle)
    override val stateOfDataInitialization = _stateOfDataInitialization.asStateFlow()

    override suspend fun dataMustBeInitialized(): Boolean? {

        return try {

            val firstLaunchEver = userOnboardingManager.checkOnboardingState()?.firstLaunchEver

            when(firstLaunchEver) {
                true -> true
                false -> false
                null -> {

                    _stateOfDataInitialization.emit(value = StateOfDataInitialization.Unsuccessful)
                    null
                }
            }
        }

        catch (e: Exception) {

            _stateOfDataInitialization.emit(value = StateOfDataInitialization.Unsuccessful)
            null
        }
    }

    override suspend fun initializeData() {

        try {

            _stateOfDataInitialization.emit(value = StateOfDataInitialization.Pending)

            val metadataEntity = MetadataEntity(
                index = 0,
                relatedUUID = null,
                signedIn = true
            )

            metadataDAO.initializeMetadata(metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()

            _stateOfDataInitialization.emit(value = StateOfDataInitialization.Successful)
        }

        catch (e: Exception) {

            _stateOfDataInitialization.emit(value = StateOfDataInitialization.Unsuccessful)
        }
    }
}