package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.repository.FirebaseRepository

class SendNotificationToTokenUseCase(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun sendNotificationToToken(notificationMessage: NotificationMessage) =
        firebaseRepository.sendNotificationToToken(notificationMessage)
}