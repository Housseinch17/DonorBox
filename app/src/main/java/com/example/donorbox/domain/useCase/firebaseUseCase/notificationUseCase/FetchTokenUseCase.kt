package com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class FetchTokenUseCase(private val firebaseRepositoryImpl: FirebaseRepositoryImpl) {
    suspend fun fetchToken(): String = firebaseRepositoryImpl.fetchToken()
}