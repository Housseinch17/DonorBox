package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class VerifyPasswordUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) =
        firebaseRepository.verifyPassword(password, onVerified, setError)
}