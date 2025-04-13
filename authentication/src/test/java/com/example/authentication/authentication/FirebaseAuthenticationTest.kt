package com.example.authentication.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FirebaseAuthenticationTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var firebaseAuthentication: Authentication

    @Before
    fun before() {

        firebaseAuth = mockk()
        firebaseUser = mockk()

        firebaseAuthentication = FirebaseAuthentication(
            firebaseAuth = firebaseAuth
        )
    }

    @Test
    fun `isSignedIn - returns true - user is not equal to null and email is verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns true

        // test
        assertTrue(firebaseAuthentication.isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false - user is not equal to null and email is not verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns false

        // test
        assertFalse(firebaseAuthentication.isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false - user is equal to null and email is not verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns null
        every { firebaseAuth.currentUser!!.isEmailVerified } returns false

        // test
        assertFalse(firebaseAuthentication.isSignedIn())
    }
}