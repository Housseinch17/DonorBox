package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class VerifyPasswordUseCase(private val firebaseRepositoryImpl: FirebaseRepositoryImpl) {
    suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) =
        firebaseRepositoryImpl.verifyPassword(password, onVerified, setError)
}