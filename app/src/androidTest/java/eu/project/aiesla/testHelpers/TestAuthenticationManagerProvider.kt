package eu.project.aiesla.testHelpers

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.di.AuthenticationManagerProvider

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