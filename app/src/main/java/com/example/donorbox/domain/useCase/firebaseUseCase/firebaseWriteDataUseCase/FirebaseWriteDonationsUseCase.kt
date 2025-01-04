package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class FirebaseWriteDonationsUseCase(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun writeDonationsIntoFirebase(username: String, donation: String) =
        firebaseRepositoryImpl.writeDonationsIntoFirebase(username, donation)
}