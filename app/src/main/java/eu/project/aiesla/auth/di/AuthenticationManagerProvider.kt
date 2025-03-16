package eu.project.aiesla.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.aiesla.auth.authentication.Authentication
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.authenticationManager.ProductionAuthenticationManager
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationManagerProvider {

    @Provides
    @Singleton
    fun provideAuthenticationManagerProduction(
        firebaseAuthentication: Authentication,
        @IoDispatcher coroutineScope: CoroutineScope,
    ): AuthenticationManager =
        ProductionAuthenticationManager(
            firebaseAuthentication = firebaseAuthentication,
            coroutineScope = coroutineScope
        )
}