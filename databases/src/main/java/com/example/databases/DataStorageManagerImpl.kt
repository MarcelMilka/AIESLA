package com.example.databases

import com.example.authentication.authentication.Authentication
import com.example.authentication.di.FirebaseAuthenticationQ
import com.example.authentication.di.RoomAuthenticationQ
import javax.inject.Inject

internal class DataStorageManagerImpl @Inject constructor(
    @FirebaseAuthenticationQ val firebaseAuthentication: Authentication,
    @RoomAuthenticationQ val roomAuthentication: Authentication,
): DataStorageManager