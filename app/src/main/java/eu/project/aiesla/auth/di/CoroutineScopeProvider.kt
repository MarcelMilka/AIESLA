package eu.project.aiesla.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineScopeProvider {

    @Provides
    @Singleton
    @IoDispatcher
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(Dispatchers.IO)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher