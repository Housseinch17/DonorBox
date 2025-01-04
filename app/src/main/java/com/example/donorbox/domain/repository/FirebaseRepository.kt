package com.example.donorbox.domain.repository

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.presentation.screens.home.FullName
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseRepository {
    suspend fun getCurrentUser(): String?
    suspend fun fetchToken(): String
    suspend fun updateDeviceToken(token: String)
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage)
    suspend fun logIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AccountStatus
    fun verifiedAccount(): Boolean
    fun signOut()
    suspend fun addUser(username: String, name: String, family: String)
    suspend fun changePassword(email: String, newPassword: String): PasswordChangement
    suspend fun resetPassword(email: String): PasswordChangement
    suspend fun readReceivers(): ReceiversResponse
    suspend fun readFullNameByUsername(): FullName
    suspend fun writeToken(username: String, token: String)
    suspend fun verifyPassword(password: String, onVerified: () -> Unit, setError: (String) -> Unit)
    suspend fun getAllReceivers(): List<String>

}