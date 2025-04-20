package com.example.databases.dataStorageManager

import com.example.authentication.authentication.Authentication
import com.example.authentication.di.FirebaseAuthenticationQ
import com.example.authentication.di.RoomAuthenticationQ
import com.example.databases.dataSynchronizationManager.DataSynchronizationManager
import com.example.datastore.data.UserOnboardingManager
import com.example.networkconnectivity.NetworkConnectivityManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStorageManagerProvider {

    @Provides
    @Singleton
    fun provideDataStorageManager(
        networkConnectivityManager: NetworkConnectivityManager,
        dataSynchronizationManager: DataSynchronizationManager,
        userOnboardingManager: UserOnboardingManager,
        @FirebaseAuthenticationQ firebaseAuthentication: Authentication,
        @RoomAuthenticationQ roomAuthentication: Authentication,
        firebaseAuth: FirebaseAuth,
    ):DataStorageManager =
        DataStorageManagerImpl(
            networkConnectivityManager = networkConnectivityManager,
            dataSynchronizationManager = dataSynchronizationManager,
            userOnboardingManager = userOnboardingManager,
            firebaseAuthentication = firebaseAuthentication,
            roomAuthentication = roomAuthentication,
            firebaseAuth = firebaseAuth,
            coroutineScope = CoroutineScope(Dispatchers.IO)
        )
}