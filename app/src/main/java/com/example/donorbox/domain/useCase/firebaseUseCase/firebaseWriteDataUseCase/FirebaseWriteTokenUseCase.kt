package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseWriteTokenUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun writeToken(username: String, token: String) = firebaseRepository.writeToken(username,token)
}