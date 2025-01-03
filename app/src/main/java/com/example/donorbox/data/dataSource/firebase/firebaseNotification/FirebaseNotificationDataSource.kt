package com.example.donorbox.data.dataSource.firebase.firebaseNotification

import com.example.donorbox.data.model.notificationMessage.NotificationMessage

interface FirebaseNotificationDataSource {
    suspend fun fetchToken(): String
    suspend fun updateDeviceToken(token: String)
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage)
}
