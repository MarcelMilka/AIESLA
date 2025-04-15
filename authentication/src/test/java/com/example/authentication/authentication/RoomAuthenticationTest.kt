package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSignUpProcess
import com.example.roomlocaldatabase.dao.MetadataDAO
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
}