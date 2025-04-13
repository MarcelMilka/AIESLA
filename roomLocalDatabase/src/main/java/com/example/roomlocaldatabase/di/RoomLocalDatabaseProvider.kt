package com.example.roomlocaldatabase.di

import android.content.Context
import androidx.room.Room
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.db.RoomLocalDatabase
import com.example.roomlocaldatabase.db.RoomLocalDatabaseConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomLocalDatabaseProvider {

    @Provides
    @Singleton
    fun provideRoomLocalDatabase(@ApplicationContext applicationContext: Context): RoomLocalDatabase =
        Room.databaseBuilder(
            context = applicationContext,
            klass = RoomLocalDatabase::class.java,
            name = RoomLocalDatabaseConstants.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideMetadataDAO(roomLocalDatabase: RoomLocalDatabase): MetadataDAO =
        roomLocalDatabase.metadataDAO()
}