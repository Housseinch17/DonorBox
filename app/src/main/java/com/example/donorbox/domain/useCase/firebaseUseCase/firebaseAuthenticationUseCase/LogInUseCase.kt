package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.login.AuthState

class LogInUseCase (
private val firebaseRepository: FirebaseRepository
) {
    suspend fun logIn(email: String,password: String): AuthState = firebaseRepository.logIn(email,password)
}