package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.home.ReceiversResponse

class FirebaseReadDataUseCaseImpl(
    private val firebaseRepository: FirebaseRepository
): FirebaseReadDataUseCase{
    override suspend fun readAllDonations(): List<String> = firebaseRepository.readAllDonations()

    override suspend fun readFullNameByUsername(): String = firebaseRepository.readFullNameByUsername()

    override suspend fun readReceivers(): ReceiversResponse = firebaseRepository.readReceivers()
}