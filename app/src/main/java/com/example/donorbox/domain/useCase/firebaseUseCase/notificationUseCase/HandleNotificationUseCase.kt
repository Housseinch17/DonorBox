package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl
import com.example.donorbox.data.model.NotificationData

class HandleNotificationUseCase(
    private val notificationDataSourceImpl: FirebaseNotificationDataSourceImpl
) {
    suspend fun handleNotification(notificationData: NotificationData) =
        notificationDataSourceImpl.handleNotification(notificationData)
}