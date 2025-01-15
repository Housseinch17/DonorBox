package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.authentication.PasswordChangement

class ChangePasswordUseCase (
private val firebaseRepository: FirebaseRepository,
) {
    suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        firebaseRepository.changePassword(email, newPassword)
}