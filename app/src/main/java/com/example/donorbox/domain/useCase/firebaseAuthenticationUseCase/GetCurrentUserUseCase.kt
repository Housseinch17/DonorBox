package com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class GetCurrentUserUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun getCurrentUser(): String? = firebaseRepositoryImpl.getCurrentUser()
}