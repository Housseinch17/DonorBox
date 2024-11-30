package com.example.donorbox.domain.repository

import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseRepository {
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut()
    suspend fun changePassword(email: String,newPassword: String): PasswordChangement
    suspend fun resetPassword(email: String): PasswordChangement
    suspend fun readReceivers(): ReceiversResponse
}