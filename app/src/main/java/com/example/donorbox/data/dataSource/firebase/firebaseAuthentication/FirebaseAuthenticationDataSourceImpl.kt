package com.example.donorbox.data.dataSource.firebase.firebaseAuthentication

import android.annotation.SuppressLint
import android.util.Log
import com.example.donorbox.presentation.screens.signup.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthenticationDataSourceImpl(
    private val auth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher,
    ) : FirebaseAuthenticationDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher){
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.e("MyTag", "getCurrentUser() ${e.message}")
            null
        }
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        withContext(coroutineDispatcher) {
            suspendCoroutine { continuation->
                try {
                    auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                            continuation.resume(PasswordChangement.Success("Password changed successfully!"))
                        } else {
                            continuation.resume(PasswordChangement.Error("Password didn't change!"))
                        }
                    }
                } catch (e: Exception) {
                    continuation.resume(PasswordChangement.Error(e.message ?: "Check your internet"))
                }
            }
        }

    override suspend fun resetPassword(email: String): PasswordChangement = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            try {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(PasswordChangement.Success("Check your email, to reset your password ! "))
                    } else {
                        continuation.resume(PasswordChangement.Error("Failed to reset password, check internet!"))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(
                    PasswordChangement.Error(
                        e.message ?: "Check your internet"
                    )
                )
            }
        }
    }

    override suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) {
        val email = auth.currentUser?.email ?: return setError("User not logged in")

        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password is correct
                    onVerified()
                } else {
                    // Password is incorrect
                    setError("Incorrect current password!")
                }
            }.addOnFailureListener { exception ->
                Log.e("MyTag","verifyPassword() error $exception")
            }
    }

    //firebase apis are callback apis so we have to use suspendCoroutine or suspendCancellableCoroutine
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            suspendCoroutine { continuation ->
                try {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            val isAccountVerified = verifiedAccount()
                            if(isAccountVerified) {
                                continuation.resume(AuthState.LoggedIn)
                            }
                            else{
                                auth.currentUser?.sendEmailVerification()
                                //sign out authentication if not verified
                                signOut()
                             continuation.resume(AuthState.Error("Error: Account not verified!"))
                            }
                        }else{
                            continuation.resume(AuthState.Error("Error: ${task.exception}"))
                        }
                    }
                }catch (e: Exception){
                    continuation.resume(AuthState.Error("Error: ${e.message}"))
                }
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun verifiedAccount(): Boolean {
        return try {
            val isVerified = auth.currentUser?.isEmailVerified
            Log.d("MyTag", "verifiedAccount() isVerified: $isVerified")
            isVerified ?: false  // Return false if isVerified is null
        } catch (e: Exception) {
            Log.e("MyTag", "Error checking email verification: ${e.message}")
            false
        }
    }

    //firebase apis are callback apis so we have to use suspendCoroutine or suspendCancellableCoroutine
    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            suspendCoroutine {  continuation ->
                try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                    if (task.isSuccessful) {
                        // Send the email verification link
                        auth.currentUser?.sendEmailVerification()
                        continuation.resume(AccountStatus.IsCreated("You successfully registered! Please verify your email."))
                    } else {
                        continuation.resume(AccountStatus.Error("Failed! ${task.exception}"))
                    }
                }
                }catch (e: Exception){
                    continuation.resume(AccountStatus.Error("${e.message}"))
                }
            }
        }


    override fun signOut(){
        auth.signOut()
    }


}

