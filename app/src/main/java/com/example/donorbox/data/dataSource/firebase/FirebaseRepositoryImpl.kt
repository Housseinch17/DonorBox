package com.example.donorbox.data.dataSource.firebase

import android.util.Log
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSource
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

class FirebaseRepositoryImpl(
    private val firebaseAuthenticationDataSource: FirebaseAuthenticationDataSource,
    private val firebaseReadDataDataSource: FirebaseReadDataSource,
    private val firebaseWriteDataSource: FirebaseWriteDataSource,
    private val firebaseNotificationDataSource: FirebaseNotificationDataSource
) : FirebaseRepository {
    override suspend fun getCurrentUser(): String? {
        return firebaseAuthenticationDataSource.getCurrentUser()
    }

    override suspend fun fetchToken(): String {
        return firebaseNotificationDataSource.fetchToken()
    }

    override suspend fun updateDeviceToken(token: String) {
        firebaseNotificationDataSource.updateDeviceToken(token)
    }

    override suspend fun sendNotificationToToken(notificationMessage: NotificationMessage) {
        Log.d("MyTag","sendNotification repositoryImpl")
        firebaseNotificationDataSource.sendNotificationToToken(notificationMessage)
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseAuthenticationDataSource.logIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseAuthenticationDataSource.signUp(email, password)
    }

    override  fun verifiedAccount(): Boolean {
        return firebaseAuthenticationDataSource.verifiedAccount()
    }

    override fun signOut() {
        return firebaseAuthenticationDataSource.signOut()
    }

    override suspend fun addUser(username: String, name: String) {
        return firebaseWriteDataSource.addUsers(username,name)
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement {
        return firebaseAuthenticationDataSource.changePassword(email, newPassword)
    }

    override suspend fun resetPassword(email: String): PasswordChangement {
        return firebaseAuthenticationDataSource.resetPassword(email)
    }

    override suspend fun readReceivers(): ReceiversResponse {
        return firebaseReadDataDataSource.readReceivers()
    }

    override suspend fun readFullNameByUsername(): String {
        Log.d("MyTag","readFullNameByUsername firebaseRepositoryImpl")
        return firebaseReadDataDataSource.readFullNameByUsername()
    }

    override suspend fun writeToken(username: String, token: String) {
        return firebaseWriteDataSource.writeToken(username, token)
    }

    override suspend fun writeDonationsIntoFirebase(username: String, donation: String) {
        firebaseWriteDataSource.writeDonationsIntoFirebase(username,donation)
    }

    override suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) {
        firebaseAuthenticationDataSource.verifyPassword(password, onVerified, setError)
    }


    override suspend fun readAllDonations(): List<String> {
        return firebaseReadDataDataSource.readAllDonations()
    }

}