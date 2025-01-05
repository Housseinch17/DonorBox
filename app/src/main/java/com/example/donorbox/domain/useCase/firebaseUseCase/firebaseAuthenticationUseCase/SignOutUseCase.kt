package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class SignOutUseCase (
private val firebaseRepository: FirebaseRepository
) {
    suspend fun signOut() = firebaseRepository.signOut()
}