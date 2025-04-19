package com.example.datastore.di

import android.content.Context
import com.example.datastore.data.UserOnboardingManager
import com.example.datastore.data.UserOnboardingManagerImpl
import com.example.datastore.data.dataStoreUserOnboardingState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserOnboardingManagerProvider {

    @Provides
    @Singleton
    fun provideUserOnboardingManager(
        @ApplicationContext context: Context
    ): UserOnboardingManager =
        UserOnboardingManagerImpl(dataStoreUserOnboardingState = context.dataStoreUserOnboardingState)
}