package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignUpProcess
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity
import javax.inject.Inject

internal class RoomAuthentication @Inject constructor(
    private val metadataDAO: MetadataDAO
): Authentication {

    override fun isSignedIn(): Boolean = this.metadataDAO.isSignedIn()

    override suspend fun signUp(credentials: EmailAndPasswordCredentials): ResultOfSignUpProcess {

        return try {

            val metadataEntity = MetadataEntity(
                index = 0,
                relatedUUID = null,
                signedIn = true
            )

            metadataDAO.initializeMetadata(metadataEntity)

            ResultOfSignUpProcess.Ok
        }

        catch (e: Exception) {

            ResultOfSignUpProcess.UnidentifiedException
        }
    }

    override suspend fun sendSignUpVerificationEmail(): ResultOfSendingSignUpVerificationEmail =
        ResultOfSendingSignUpVerificationEmail.Ok
}