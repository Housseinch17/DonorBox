package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

class FirebaseReadReceiversUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun readReceivers(): ReceiversResponse = firebaseRepository.readReceivers()
}