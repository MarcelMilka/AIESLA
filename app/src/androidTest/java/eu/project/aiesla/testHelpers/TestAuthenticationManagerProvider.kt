package eu.project.aiesla.testHelpers

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.project.aiesla.auth.authenticationManager.AuthenticationManager
import eu.project.aiesla.auth.di.AuthenticationManagerProvider
import eu.project.aiesla.auth.di.IoDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthenticationManagerProvider::class]
)
class TestAuthenticationManagerProvider {

    @Provides
    fun provideTestAuthenticationManager(
        @IoDispatcher coroutineScope: CoroutineScope
    ): AuthenticationManager =
        TestAuthenticationManager(coroutineScope)
}