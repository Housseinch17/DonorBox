package com.example.donorbox.data.dataSource.firebase.firebaseAuthentication

import android.util.Log
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthenticationDataSourceImpl (
    private val auth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher,
    ) : FirebaseAuthenticationDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher){
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.d("MyTag", "getCurrentUser() ${e.message}")
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
                Log.d("MyTag", "f1")
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

    //firebase login is asynchronous using await ensuring that it has to wait
    //for  signInWithEmailAndPassword to complete same as using async await
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            return@withContext try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthState.LoggedIn

            } catch (e: Exception) {
                val errorMessage = e.message ?: "Something went wrong"
                AuthState.Error(errorMessage)
            }
        }

    //firebase signup is asynchronous using await ensuring that it has to wait
    //for  createUserWithEmailAndPassword to complete same as using async await
    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            return@withContext try {
                val deferred1 = async {
                    auth.createUserWithEmailAndPassword(email, password)
                }.await()
                AccountStatus.IsCreated("Account Created")
            } catch (e: Exception) {
                AccountStatus.Error(e.message ?: "Check your Internet")
            }
        }


    override suspend fun signOut() = withContext(coroutineDispatcher) {
        auth.signOut()
    }


}

