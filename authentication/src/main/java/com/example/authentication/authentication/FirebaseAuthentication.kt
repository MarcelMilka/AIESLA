package com.example.authentication.authentication

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

internal class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): Authentication {

    override fun isSignedIn(): Boolean =
        firebaseAuth.currentUser != null && firebaseAuth.currentUser!!.isEmailVerified
}