package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus

class SignUpUseCase (
private val firebaseRepository: FirebaseRepository
) {
    suspend fun signUp(email: String,password: String): AccountStatus = firebaseRepository.signUp(email,password)
}