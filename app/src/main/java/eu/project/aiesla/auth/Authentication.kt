package eu.project.aiesla.auth

interface Authentication {

    fun isSignedIn(): Boolean

    suspend fun signUpWithEmailAndPassword(email: String, password: String): ResultOfSignUpProcess

    suspend fun signInWithEmailAndPassword(email: String, password: String): ResultOfSignInProcess

    suspend fun sendSignUpVerificationEmail(): ResultOfVerificationProcess
}