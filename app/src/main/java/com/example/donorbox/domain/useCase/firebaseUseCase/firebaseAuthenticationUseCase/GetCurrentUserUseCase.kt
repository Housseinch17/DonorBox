package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class GetCurrentUserUseCase (
private val firebaseRepository: FirebaseRepository
) {
    suspend fun getCurrentUser(): String? = firebaseRepository.getCurrentUser()
}