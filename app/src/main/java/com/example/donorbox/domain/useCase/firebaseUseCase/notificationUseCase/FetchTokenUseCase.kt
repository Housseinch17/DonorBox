package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FetchTokenUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun fetchToken(): String = firebaseRepository.fetchToken()
}