package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class UpdateDeviceTokenUseCase(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun updateDeviceToken(token: String = "") = firebaseRepository.updateDeviceToken(token)
}