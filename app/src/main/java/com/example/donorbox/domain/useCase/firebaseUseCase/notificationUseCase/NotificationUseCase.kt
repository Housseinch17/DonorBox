package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.model.notificationMessage.NotificationMessage

interface NotificationUseCase {
    suspend fun fetchToken(): String
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage)
    suspend fun updateDeviceToken(token: String = "")
}