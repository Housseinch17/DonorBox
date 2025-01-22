package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

interface FirebaseWriteDataUseCase {
    suspend fun addUser(username: String, name: String)
    suspend fun writeDonationsIntoFirebase(username: String, donation: String)
    suspend fun writeToken(username: String, token: String)
}