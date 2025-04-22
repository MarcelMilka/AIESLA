package com.example.databases.common

import com.example.databases.roomLocalDatabase.RoomLocalDatabaseManager
import com.example.datastore.data.UserOnboardingManager
import com.example.roomlocaldatabase.dao.MetadataDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseManagerProvider {

    @Provides
    @Singleton
    fun provideRoomLocalDatabaseManager(
        userOnboardingManager: UserOnboardingManager,
        metadataDAO: MetadataDAO
    ): DatabaseManager =
        RoomLocalDatabaseManager(
            userOnboardingManager = userOnboardingManager,
            metadataDAO = metadataDAO
        )
}