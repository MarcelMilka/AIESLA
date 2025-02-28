package eu.project.aiesla.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.authenticationManager.TestAuthenticationManager

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthenticationManagerProvider::class]
)
class TestAuthenticationManagerProvider {

    @Provides
    fun provideTestAuthenticationManager(): AuthenticationManager =
        TestAuthenticationManager()
}