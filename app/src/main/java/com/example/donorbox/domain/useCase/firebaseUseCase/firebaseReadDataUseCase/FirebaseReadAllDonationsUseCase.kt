package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class FirebaseReadAllDonationsUseCase(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun readAllDonations(): List<String> = firebaseRepositoryImpl.readAllDonations()
}