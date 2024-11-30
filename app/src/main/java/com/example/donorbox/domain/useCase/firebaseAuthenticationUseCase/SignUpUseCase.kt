package com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus

class SignUpUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun signUp(email: String,password: String): AccountStatus = firebaseRepositoryImpl.signUp(email,password)
}