package com.example.authentication.di

import com.example.authentication.authentication.Authentication
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.authentication.authenticationManager.AuthenticationManagerImpl
import com.example.datastore.data.OnboardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationManagerProvider {

    @Provides
    @Singleton
    fun provideAuthenticationManagerImpl(
        onboardingRepository: OnboardingRepository,
        @FirebaseAuthenticationQ firebaseAuthentication: Authentication,
        @RoomAuthenticationQ roomAuthentication: Authentication,
    ): AuthenticationManager =
        AuthenticationManagerImpl(
            onboardingRepository = onboardingRepository,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            coroutineScope = CoroutineScope(Dispatchers.IO)
        )
}