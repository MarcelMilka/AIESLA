package com.example.databases.dataSynchronizationManager

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSynchronizationManagerProvider {

    @Provides
    @Singleton
    fun provideDataSynchronizationManager(): DataSynchronizationManager =
        DataSynchronizationManagerImpl()
}