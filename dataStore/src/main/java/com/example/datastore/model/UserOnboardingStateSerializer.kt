package com.example.datastore.model

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserOnboardingStateSerializer: Serializer<UserOnboardingState> {

    override val defaultValue: UserOnboardingState
        get() = UserOnboardingState()

    override suspend fun readFrom(input: InputStream): UserOnboardingState {

        return try {

            Json.decodeFromString(
                deserializer = UserOnboardingState.serializer(),
                input.readBytes().decodeToString()
            )
        }

        catch (e: SerializationException) {

            UserOnboardingState(firstLaunchEver = null)
        }
    }

    override suspend fun writeTo(t: UserOnboardingState, output: OutputStream) {

        output.write(
            Json.encodeToString(
                serializer = UserOnboardingState.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}