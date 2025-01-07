package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseReadFullNameUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun readFullNameByUsername(): String =
        firebaseRepository.readFullNameByUsername()
}