package com.example.authentication.authentication

import com.example.authentication.credentials.CloudUid
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.results.ResultOfPasswordRecoveryProcess
import com.example.authentication.results.ResultOfSendingSignUpVerificationEmail
import com.example.authentication.results.ResultOfSignInProcess
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
    private lateinit var authTask: Task<AuthResult>
    private lateinit var task: Task<Void>

    private lateinit var firebaseAuthentication: Authentication

    @Before
    fun before() {

        firebaseAuth = mockk()
        firebaseUser = mockk()
        authTask = mockk()
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
    fun `getCloudUid - returns UID when user is signed in`() = runTest {

        val expectedUid = "test_uid"

        // stubbing
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns expectedUid

        // testing
        val result = firebaseAuthentication.getCloudUid()
        assertEquals(CloudUid(uid = expectedUid), result)
    }

    @Test
    fun `getCloudUid - returns null when no user is signed in`() = runTest {

        // stubbing
        every { firebaseAuth.currentUser } returns null

        // testing
        val result = firebaseAuthentication.getCloudUid()
        assertEquals(CloudUid(uid = null), result)
    }

    @Test
    fun `getCloudUid - returns null when exception is thrown`() = runTest {

        // stubbing
        every { firebaseAuth.currentUser } throws RuntimeException("Unexpected error")

        // testing
        val result = firebaseAuthentication.getCloudUid()
        assertEquals(CloudUid(uid = null), result)
    }



    @Test
    fun `signUp - returns Ok when the sign up process is successful`() = runTest {

        // stubbing
        every { authTask.isSuccessful } returns true
        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseUser.isEmailVerified } returns false
        every { firebaseAuth.currentUser } returns firebaseUser

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTask

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

        every { authTask.isSuccessful } returns false
        every { authTask.exception } returns exception

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTask

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.InvalidEmailFormat, result)
    }

    @Test
    fun `signUp - returns EmailIsAlreadyInUse when email already in use`() = runTest {

        // stubbing
        val exception = mockk<FirebaseAuthUserCollisionException>()

        every { authTask.isSuccessful } returns false
        every { authTask.exception } returns exception

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTask

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.EmailIsAlreadyInUse, result)
    }

    @Test
    fun `signUp - returns UnidentifiedException when an exception occurs`() = runTest {

        // stubbing
        val unknownException = Exception("Some generic error")

        every { authTask.isSuccessful } returns false
        every { authTask.exception } returns unknownException

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTask

        val result = firebaseAuthentication.signUp(emailAndPasswordCredentials)

        // testing
        assertEquals(ResultOfSignUpProcess.UnidentifiedException, result)
    }



    @Test
    fun `sendSignUpVerificationEmail - returns Ok when the verification email is sent successfully`() = runTest {

        // stubbing
        every { task.isSuccessful } returns true

        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseUser.sendEmailVerification() } returns task
        every { firebaseAuth.currentUser } returns firebaseUser

        // testing
        val result = firebaseAuthentication.sendSignUpVerificationEmail()
        assertEquals(ResultOfSendingSignUpVerificationEmail.Ok, result)
    }

    @Test
    fun `sendSignUpVerificationEmail - returns Ok when the verification email is sent unsuccessfully`() = runTest {

        // stubbing
        every { task.isSuccessful } returns false

        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseUser.sendEmailVerification() } returns task
        every { firebaseAuth.currentUser } returns firebaseUser

        // testing
        val result = firebaseAuthentication.sendSignUpVerificationEmail()
        assertEquals(ResultOfSendingSignUpVerificationEmail.UnidentifiedException, result)
    }



    @Test
    fun `signIn - returns Ok when sign in succeeds and user is verified`() = runTest {

        // stubbing
        every { authTask.isSuccessful } returns true
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.isEmailVerified } returns true

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns authTask

        // testing
        val result = firebaseAuthentication.signIn(emailAndPasswordCredentials)
        assertEquals(ResultOfSignInProcess.Ok, result)
    }

    @Test
    fun `signIn - returns InvalidEmailFormat when exception message indicates badly formatted email`() = runTest {

        // mocking
        every { authTask.isSuccessful } returns false
        every { authTask.exception?.message } returns "The email address is badly formatted"
        every { firebaseAuth.currentUser } returns null

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns authTask

        // testing
        val result = firebaseAuthentication.signIn(emailAndPasswordCredentials)
        assertEquals(ResultOfSignInProcess.InvalidEmailFormat, result)
    }

    @Test
    fun `signIn - returns PasswordIsIncorrect when exception message indicates bad credentials`() = runTest {

        // stubbing
        every { authTask.isSuccessful } returns false
        every { authTask.exception?.message } returns "The supplied auth credential is incorrect, malformed or has expired"
        every { firebaseAuth.currentUser } returns null

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns authTask

        // testing
        val result = firebaseAuthentication.signIn(emailAndPasswordCredentials)
        assertEquals(ResultOfSignInProcess.PasswordIsIncorrect, result)
    }

    @Test
    fun `signIn - returns UnidentifiedException when exception message is unknown`() = runTest {

        // stubbing
        every { authTask.isSuccessful } returns false
        every { authTask.exception?.message } returns "Something weird happened"
        every { firebaseAuth.currentUser } returns null

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns authTask

        // testing
        val result = firebaseAuthentication.signIn(emailAndPasswordCredentials)

        assertEquals(ResultOfSignInProcess.UnidentifiedException, result)
    }

    @Test
    fun `signIn - returns UnidentifiedException when exception is null`() = runTest {

        // stubbing
        every { authTask.isSuccessful } returns false
        every { authTask.exception } returns null
        every { firebaseAuth.currentUser } returns null

        every { authTask.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(authTask)
            authTask
        }

        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns authTask

        // testing
        val result = firebaseAuthentication.signIn(emailAndPasswordCredentials)

        assertEquals(ResultOfSignInProcess.UnidentifiedException, result)
    }



    @Test
    fun `sendPasswordRecoveryEmail - returns Ok when task is successful`() = runTest {

        // stubbing
        every { task.isSuccessful } returns true
        every { task.addOnCompleteListener(any()) } answers {

            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(task)
            task
        }
        every { firebaseAuth.sendPasswordResetEmail(any()) } returns task

        // testing
        val result = firebaseAuthentication.sendPasswordRecoveryEmail(EmailCredential("user@example.com"))

        assertEquals(ResultOfPasswordRecoveryProcess.Ok, result)
    }

    @Test
    fun `sendPasswordRecoveryEmail - returns UnidentifiedException for unknown error`() = runTest {

        // stubbing
        every { task.isSuccessful } returns false
        every { task.exception } returns Exception("Some unknown exception")
        every { task.addOnCompleteListener(any()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(task)
            task
        }

        every { firebaseAuth.sendPasswordResetEmail(any()) } returns task

        // testing
        val result = firebaseAuthentication.sendPasswordRecoveryEmail(EmailCredential("user@example.com"))

        assertEquals(ResultOfPasswordRecoveryProcess.UnidentifiedException, result)
    }
}