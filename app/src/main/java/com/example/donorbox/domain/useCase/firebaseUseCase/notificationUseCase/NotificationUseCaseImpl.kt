package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.repository.FirebaseRepository

class NotificationUseCaseImpl(private val firebaseRepository: FirebaseRepository
): NotificationUseCase{
    override suspend fun fetchToken(): String = firebaseRepository.fetchToken()

    override suspend fun sendNotificationToToken(notificationMessage: NotificationMessage) =
        firebaseRepository.sendNotificationToToken(notificationMessage)

    override suspend fun updateDeviceToken(token: String) = firebaseRepository.updateDeviceToken(token)
}