package com.example.authentication.di

import com.example.authentication.authentication.Authentication
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.authentication.authentication.FirebaseAuthentication
import com.example.authentication.authentication.RoomAuthentication
import com.example.roomlocaldatabase.dao.MetadataDAO

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationProvider {

    @Provides
    @Singleton
    @FirebaseAuthenticationQ
    fun provideFirebaseAuthentication(
        firebaseAuth: FirebaseAuth
    ): Authentication = FirebaseAuthentication(firebaseAuth = firebaseAuth)

    @Provides
    @Singleton
    @RoomAuthenticationQ
    fun provideRoomAuthentication(
        metadataDAO: MetadataDAO
    ): Authentication = RoomAuthentication(metadataDAO = metadataDAO)
}