package com.example.datastore.data

import android.content.Context
import androidx.datastore.dataStore
import com.example.datastore.model.UserOnboardingStateSerializer

val Context.dataStoreUserOnboardingState by dataStore(
    fileName = "user-onboarding-state.json",
    serializer = UserOnboardingStateSerializer
)