package com.example.authentication.authentication

import com.example.roomlocaldatabase.dao.MetadataDAO
import javax.inject.Inject

internal class RoomAuthentication @Inject constructor(
    private val metadataDAO: MetadataDAO
): Authentication {

    override fun isSignedIn(): Boolean = this.metadataDAO.isSignedIn()
}