package com.example.donorbox.domain.repository

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import com.example.donorbox.presentation.screens.home.ReceiversResponse
import com.example.donorbox.presentation.screens.login.AuthState
import com.example.donorbox.presentation.screens.signup.AccountStatus

interface FirebaseRepository {
    suspend fun getCurrentUser(): String?
    suspend fun fetchToken(): String
    suspend fun updateDeviceToken(token: String)
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage)
    suspend fun logIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AccountStatus
    fun verifiedAccount(): Boolean
    fun signOut()
    suspend fun addUser(username: String, name: String)
    suspend fun changePassword(email: String, newPassword: String): PasswordChangement
    suspend fun resetPassword(email: String): PasswordChangement
    suspend fun readReceivers(): ReceiversResponse
    suspend fun readFullNameByUsername(): String
    suspend fun writeToken(username: String, token: String)
    suspend fun writeDonationsIntoFirebase(username: String, donation: String)
    suspend fun verifyPassword(password: String, onVerified: () -> Unit, setError: (String) -> Unit)
    suspend fun readAllDonations(): List<String>

}