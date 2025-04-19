package com.example.datastore.data

import com.example.datastore.model.UserOnboardingState

interface UserOnboardingManager {

    suspend fun checkOnboardingState(): UserOnboardingState?

    suspend fun setOnboardingStateToFalse()
}