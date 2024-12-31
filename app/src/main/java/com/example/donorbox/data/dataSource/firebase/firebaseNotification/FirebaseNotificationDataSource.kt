package com.example.donorbox.data.dataSource.firebase.firebaseNotification

import com.example.donorbox.data.model.NotificationData

interface FirebaseNotificationDataSource {
    suspend fun fetchToken(): String
    suspend fun updateDeviceToken()
    suspend fun handleNotification(notificationData: NotificationData)
}
