package com.example.donorbox.data.dataSource.firebase

import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSourceImpl
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.home.FullName
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

class FirebaseRepositoryImpl(
    private val firebaseAuthenticationDataSourceImpl: FirebaseAuthenticationDataSourceImpl,
    private val firebaseReadDataSourceImpl: FirebaseReadDataSourceImpl,
    private val firebaseWriteDataSourceImpl: FirebaseWriteDataSourceImpl,
    private val notificationDataSourceImpl: FirebaseNotificationDataSourceImpl
) : FirebaseRepository {
    override suspend fun getCurrentUser(): String? {
        return firebaseAuthenticationDataSourceImpl.getCurrentUser()
    }

    override suspend fun fetchToken(): String {
        return notificationDataSourceImpl.fetchToken()
    }

    override suspend fun updateDeviceToken(token: String) {
        notificationDataSourceImpl.updateDeviceToken(token)
    }

    override suspend fun sendNotificationToToken(notificationMessage: NotificationMessage) {
        notificationDataSourceImpl.sendNotificationToToken(notificationMessage)
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseAuthenticationDataSourceImpl.logIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseAuthenticationDataSourceImpl.signUp(email, password)
    }

    override  fun verifiedAccount(): Boolean {
        return firebaseAuthenticationDataSourceImpl.verifiedAccount()
    }

    override fun signOut() {
        return firebaseAuthenticationDataSourceImpl.signOut()
    }

    override suspend fun addUser(username: String, name: String, family: String) {
        return firebaseWriteDataSourceImpl.addUsers(username,name,family)
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement {
        return firebaseAuthenticationDataSourceImpl.changePassword(email, newPassword)
    }

    override suspend fun resetPassword(email: String): PasswordChangement {
        return firebaseAuthenticationDataSourceImpl.resetPassword(email)
    }

    override suspend fun readReceivers(): ReceiversResponse {
        return firebaseReadDataSourceImpl.readReceivers()
    }

    override suspend fun readFullNameByUsername(): FullName {
        return firebaseReadDataSourceImpl.readFullNameByUsername()
    }

    override suspend fun writeToken(username: String, token: String) {
        return firebaseWriteDataSourceImpl.writeToken(username, token)
    }

    override suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) {
        firebaseAuthenticationDataSourceImpl.verifyPassword(password, onVerified, setError)
    }

    override suspend fun getAllReceivers(): List<String> {
        return firebaseReadDataSourceImpl.getAllReceivers()
    }

}