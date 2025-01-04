package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl

class UpdateDeviceTokenUseCase(
    private val notificationDataSourceImpl: FirebaseNotificationDataSourceImpl
) {
    suspend fun updateDeviceToken(token: String = "") = notificationDataSourceImpl.updateDeviceToken(token)
}