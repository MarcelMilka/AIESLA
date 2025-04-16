package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.ResultOfPasswordRecoveryProcess
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
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

    override suspend fun signIn(credentials: EmailAndPasswordCredentials): ResultOfSignInProcess {

        return try {

            metadataDAO.signIn()
            ResultOfSignInProcess.Ok
        }

        catch (e: Exception) {

            ResultOfSignInProcess.UnidentifiedException
        }
    }

    override suspend fun sendPasswordRecoveryEmail(email: EmailCredential): ResultOfPasswordRecoveryProcess =
        ResultOfPasswordRecoveryProcess.Ok
}