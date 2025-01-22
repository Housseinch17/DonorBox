package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.presentation.screens.home.ReceiversResponse

interface FirebaseReadDataUseCase {
    suspend fun readAllDonations(): List<String>
    suspend fun readFullNameByUsername(): String
    suspend fun readReceivers(): ReceiversResponse
}