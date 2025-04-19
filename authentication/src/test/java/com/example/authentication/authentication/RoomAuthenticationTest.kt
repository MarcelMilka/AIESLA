package com.example.authentication.authentication

import com.example.authentication.credentials.CloudUid
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.ResultOfPasswordRecoveryProcess
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
import com.example.authentication.results.ResultOfSignUpProcess
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RoomAuthenticationTest {

    private lateinit var metadataDAO: MetadataDAO
    private lateinit var roomAuthentication: Authentication

    @Before
    fun before() {

        metadataDAO = mockk()

        roomAuthentication = RoomAuthentication(
            metadataDAO = metadataDAO
        )
    }

    private val emailAndPasswordCredentials = EmailAndPasswordCredentials(
        email = "valid.email@gmail.com",
        password = "properlyFormattedPassword"
    )


    @Test
    fun `isSignedIn - returns true when user is signed in`() {

        // stubbing
        every { metadataDAO.isSignedIn() } returns true

        // test
        assertTrue(roomAuthentication.isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false when user is not signed in`() {

        // stubbing
        every { metadataDAO.isSignedIn() } returns false

        // test
        assertFalse(roomAuthentication.isSignedIn())
    }



    @Test
    fun `getCloudUid - MetadataEntity is not null, uid is not null - CloudUid with uid is returned`() = runTest {

        val metadataEntity = MetadataEntity(0, "SomeUUID", true)
        val cloudUid = CloudUid(metadataEntity.relatedUUID)

        // stubbing
        coEvery { metadataDAO.getMetadata() } returns metadataEntity

        // test
        assertEquals(
            cloudUid,
            roomAuthentication.getCloudUid()
        )
    }

    @Test
    fun `getCloudUid - MetadataEntity is not null, uid is null - CloudUid with null is returned`() = runTest {

        val metadataEntity = MetadataEntity(0, null, true)
        val cloudUid = CloudUid(metadataEntity.relatedUUID)

        // stubbing
        coEvery { metadataDAO.getMetadata() } returns metadataEntity

        // test
        assertEquals(
            cloudUid,
            roomAuthentication.getCloudUid()
        )
    }

    @Test
    fun `getCloudUid - MetadataEntity is null - null is returned`() = runTest {

        // stubbing
        coEvery { metadataDAO.getMetadata() } returns null

        // test
        assertNull(roomAuthentication.getCloudUid())
    }

    @Test
    fun `getCloudUid - Exception is thrown - null is returned`() = runTest {

        // stubbing
        coEvery { metadataDAO.getMetadata() } throws Exception()

        // test
        assertNull(roomAuthentication.getCloudUid())
    }



    @Test
    fun `signUp - returns Ok when instance of MetadataEntity is properly inserted`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = any()) } just runs

        // test
        assertEquals(
            ResultOfSignUpProcess.Ok,
            roomAuthentication.signUp(credentials = emailAndPasswordCredentials)
        )
    }

    @Test
    fun `signUp - returns UnidentifiedException when instance of MetadataEntity is not properly inserted`() = runTest {

        // stubbing
        coEvery { metadataDAO.initializeMetadata(metadataEntity = any()) } throws RuntimeException()

        // test
        assertEquals(
            ResultOfSignUpProcess.UnidentifiedException,
            roomAuthentication.signUp(credentials = emailAndPasswordCredentials)
        )
    }



    @Test
    fun `sendSignUpVerificationEmail - always returns Ok`() = runTest {

        assertEquals(
            ResultOfSendingSignUpVerificationEmail.Ok,
            roomAuthentication.sendSignUpVerificationEmail()
        )
    }



    @Test
    fun `signIn - returns Ok when user is properly signed in`() = runTest {

        // stubbing
        coEvery { metadataDAO.signIn() } just runs

        // test
        assertEquals(
            ResultOfSignInProcess.Ok,
            roomAuthentication.signIn(credentials = emailAndPasswordCredentials)
        )
    }

    @Test
    fun `signIn - returns UnidentifiedException when an error occurs`() = runTest {

        // stubbing
        coEvery { metadataDAO.signIn() } throws RuntimeException()

        // test
        assertEquals(
            ResultOfSignInProcess.UnidentifiedException,
            roomAuthentication.signIn(credentials = emailAndPasswordCredentials)
        )
    }



    @Test
    fun `sendPasswordRecoveryEmail - always returns Ok`() = runTest {

        assertEquals(
            ResultOfPasswordRecoveryProcess.Ok,
            roomAuthentication.sendPasswordRecoveryEmail(email = EmailCredential(""))
        )
    }
}