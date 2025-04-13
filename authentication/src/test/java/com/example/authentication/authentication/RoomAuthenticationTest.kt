package com.example.authentication.authentication

import com.example.roomlocaldatabase.dao.MetadataDAO
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
}