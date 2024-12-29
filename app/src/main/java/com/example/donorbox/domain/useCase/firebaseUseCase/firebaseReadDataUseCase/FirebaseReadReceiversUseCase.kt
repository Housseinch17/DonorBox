package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

class FirebaseReadReceiversUseCase(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun readReceivers(): ReceiversResponse = firebaseRepositoryImpl.readReceivers()
}