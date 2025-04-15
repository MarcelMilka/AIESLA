package com.example.authentication.authentication

import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.results.ResultOfSignUpProcess
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FirebaseAuthenticationTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var task: Task<AuthResult>

    private lateinit var firebaseAuthentication: Authentication

    @Before
    fun before() {

        firebaseAuth = mockk()
        firebaseUser = mockk()
        task = mockk()

        firebaseAuthentication = FirebaseAuthentication(
            firebaseAuth = firebaseAuth
        )
    }

    private val emailAndPasswordCredentials = EmailAndPasswordCredentials(
        email = "valid.email@gmail.com",
        password = "properlyFormattedPassword"
    )



    @Test
    fun `isSignedIn - returns true - user is not equal to null and email is verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns true

        // testing
        assertTrue(firebaseAuthentication.isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false - user is not equal to null and email is not verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns false

        // testing
        assertFalse(firebaseAuthentication.isSignedIn())
    }

    @Test
    fun `isSignedIn - returns false - user is equal to null and email is not verified`() {

        // stubbing
        every { firebaseAuth.currentUser } returns null
        every { firebaseAuth.currentUser!!.isEmailVerified } returns false

        // testing
        assertFalse(firebaseAuthentication.isSignedIn())
    }



    @Test
    fun `signUp - returns Ok when the sign up process is successful`() = runTest {

        // stubbing
        every { task.isSuccessful } returns true
        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseUser.isEmailVerified } returns false
        every { firebaseAuth.currentUser } returns firebaseUser

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task

        // testing
        assertEquals(
            ResultOfSignUpProcess.Ok,
            firebaseAuthentication.signUp(emailAndPasswordCredentials)
        )
    }

    @Test
    fun `signUp - returns InvalidEmailFormat when email does not meet requirements`() = runTest {

        // stubbing
        val exception = mockk<FirebaseAuthInvalidCredentialsException>()

        every { task.isSuccessful } returns false
        every { task.exception } returns exception

        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.InvalidEmailFormat, result)
    }

    @Test
    fun `signUp - returns EmailIsAlreadyInUse when email already in use`() = runTest {

        // stubbing
        val exception = mockk<FirebaseAuthUserCollisionException>()

        every { task.isSuccessful } returns false
        every { task.exception } returns exception

        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.EmailIsAlreadyInUse, result)
    }

    @Test
    fun `signUp - returns UnidentifiedException when an exception occurs`() = runTest {

        // stubbing
        val unknownException = Exception("Some generic error")

        every { task.isSuccessful } returns false
        every { task.exception } returns unknownException

        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.UnidentifiedException, result)
    }
}