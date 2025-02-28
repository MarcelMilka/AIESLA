package eu.project.aiesla.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.aiesla.auth.authentication.Authentication
import eu.project.aiesla.auth.authentication.FirebaseAuthentication
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationProvider {

    @Provides
    @Singleton
    fun provideFirebaseAuthentication(): Authentication =
        FirebaseAuthentication()
}