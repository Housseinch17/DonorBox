package com.example.donorbox.data.dataSource.firebase.firebaseAuthentication

import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import com.example.donorbox.presentation.screens.login.AuthState
import com.example.donorbox.presentation.screens.signup.AccountStatus

interface FirebaseAuthenticationDataSource {
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    fun verifiedAccount(): Boolean
    fun signOut()
    suspend fun changePassword(email: String,newPassword: String): PasswordChangement
    suspend fun resetPassword(email: String): PasswordChangement
    suspend fun verifyPassword(password: String, onVerified: () -> Unit, setError: (String) -> Unit)

}