package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseWriteDonationsUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun writeDonationsIntoFirebase(username: String, donation: String) =
        firebaseRepository.writeDonationsIntoFirebase(username, donation)
}