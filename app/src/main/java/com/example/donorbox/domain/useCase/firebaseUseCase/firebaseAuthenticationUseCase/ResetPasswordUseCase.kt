package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase


import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement

class ResetPasswordUseCase (
private val firebaseRepository: FirebaseRepository
) {
    suspend fun resetPassword(email: String): PasswordChangement = firebaseRepository.resetPassword(email)
}