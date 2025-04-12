package com.example.datastore.di

import android.content.Context
import com.example.datastore.data.OnboardingRepository
import com.example.datastore.data.dataStoreUserOnboardingState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OnboardingRepositoryProvider {

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        @ApplicationContext context: Context
    ): OnboardingRepository =
        OnboardingRepository(
            dataStoreUserOnboardingState = context.dataStoreUserOnboardingState
        )
}