package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseReadAllDonationsUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun readAllDonations(): List<String> = firebaseRepository.readAllDonations()
}