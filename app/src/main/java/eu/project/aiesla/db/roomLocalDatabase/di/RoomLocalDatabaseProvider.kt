package eu.project.aiesla.db.roomLocalDatabase.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.project.aiesla.db.roomLocalDatabase.dao.SubjectDAO
import eu.project.aiesla.db.roomLocalDatabase.db.RoomLocalDatabaseConstants
import eu.project.aiesla.db.roomLocalDatabase.db.RoomLocalDatabase
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
            name = RoomLocalDatabaseConstants.ROOM_V1
        ).build()

    @Provides
    @Singleton
    fun provideSubjectDAO(roomLocalDatabase: RoomLocalDatabase): SubjectDAO =
        roomLocalDatabase.subjectDAO()
}