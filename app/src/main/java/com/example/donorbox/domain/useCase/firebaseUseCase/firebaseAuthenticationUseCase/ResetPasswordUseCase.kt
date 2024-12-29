package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase


import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement

class ResetPasswordUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun resetPassword(email: String): PasswordChangement = firebaseRepositoryImpl.resetPassword(email)
}