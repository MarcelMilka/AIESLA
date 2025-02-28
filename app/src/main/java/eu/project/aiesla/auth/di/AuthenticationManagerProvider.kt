package eu.project.aiesla.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.aiesla.auth.authentication.Authentication
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.authenticationManager.ProductionAuthenticationManager

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationManagerProvider {

    @Provides
    fun provideAuthenticationManagerProduction(firebaseAuthentication: Authentication): AuthenticationManager =
        ProductionAuthenticationManager(
            firebaseAuthentication = firebaseAuthentication
        )
}