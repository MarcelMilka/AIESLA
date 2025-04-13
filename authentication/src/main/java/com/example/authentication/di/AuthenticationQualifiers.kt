package com.example.authentication.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class FirebaseAuthenticationQ

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RoomAuthenticationQ