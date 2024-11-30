package com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement

class ChangePasswordUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        firebaseRepositoryImpl.changePassword(email, newPassword)
}