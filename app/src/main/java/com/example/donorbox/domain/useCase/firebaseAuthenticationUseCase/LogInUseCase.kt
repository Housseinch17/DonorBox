package com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.sealedInterfaces.AuthState

class LogInUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun logIn(email: String,password: String): AuthState = firebaseRepositoryImpl.logIn(email,password)
}