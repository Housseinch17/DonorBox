package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import com.example.donorbox.presentation.screens.login.AuthState
import com.example.donorbox.presentation.screens.signup.AccountStatus

interface AuthenticationUseCase {
    suspend fun changePassword(email: String, newPassword: String): PasswordChangement
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun resetPassword(email: String): PasswordChangement
    suspend fun signOut()
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun verifyPassword(password: String, onVerified: () -> Unit, setError: (String) -> Unit)
}