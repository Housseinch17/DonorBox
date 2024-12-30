package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class FirebaseGetAllReceiversUseCase(private val firebaseRepositoryImpl: FirebaseRepositoryImpl) {
    suspend fun getAllReceivers(): List<String> = firebaseRepositoryImpl.getAllReceivers()
}