package com.example.databases.roomLocalDatabase

import com.example.databases.common.DatabaseManager
import com.example.databases.common.ResultOfDataInitialization
import com.example.datastore.data.UserOnboardingManager
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity
import javax.inject.Inject

internal class RoomLocalDatabaseManager @Inject constructor(
    val userOnboardingManager: UserOnboardingManager,
    val metadataDAO: MetadataDAO
): DatabaseManager {

    override suspend fun dataMustBeInitialized(): Boolean? {

        return try {

            val firstLaunchEver = userOnboardingManager.checkOnboardingState()?.firstLaunchEver

            when(firstLaunchEver) {
                true -> true
                false -> false
                null -> null
            }
        }

        catch (e: Exception) {

            null
        }
    }

    override suspend fun initializeData(): ResultOfDataInitialization {

        return try {

            val metadataEntity = MetadataEntity(
                index = 0,
                relatedUUID = null,
                signedIn = true
            )

            metadataDAO.initializeMetadata(metadataEntity)
            userOnboardingManager.setOnboardingStateToFalse()

            ResultOfDataInitialization.Successful
        }

        catch (e: Exception) {

            ResultOfDataInitialization.Unsuccessful
        }
    }
}