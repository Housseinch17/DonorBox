package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl
import com.example.donorbox.data.model.notificationMessage.NotificationMessage

class SendNotificationToTokenUseCase(
    private val notificationDataSourceImpl: FirebaseNotificationDataSourceImpl
) {
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage) =
        notificationDataSourceImpl.sendNotificationToToken(notificationMessage)
}