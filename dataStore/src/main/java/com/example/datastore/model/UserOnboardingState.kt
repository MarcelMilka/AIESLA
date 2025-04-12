package com.example.datastore.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class UserOnboardingState @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val firstLaunchEver: Boolean? = true
)